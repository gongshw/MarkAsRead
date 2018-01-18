package com.gongshw.idea.mar;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.gongshw.idea.mar.domain.FileState;
import com.gongshw.idea.mar.domain.LineStatus;
import com.gongshw.idea.mar.domain.MarState;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

import lombok.Getter;

/**
 * @author gongshiwei@baidu.com
 */
@Getter
public class MarToolWindow {
    private JPanel content;
    private JTable table;

    private Project project;
    private DefaultTableModel model;
    private MarService marService;

    public MarToolWindow(Project project) {
        this.project = project;
    }

    public void init() {
        JTable table = getTable();

        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.setColumnCount(2);
        model.setColumnIdentifiers(new String[] {"File", "Lines"});
        table.setModel(model);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row < 0 || row >= table.getRowCount()) {
                    return;
                }
                String fileName = table.getValueAt(row, 0).toString();
                String basePath = project.getBasePath();
                VirtualFile file = LocalFileSystem.getInstance().findFileByPath(basePath + fileName);
                if (file == null) {
                    return;
                }
                new OpenFileDescriptor(project, file).navigate(true);
            }
        });

        marService = MarService.getInstance(project);
        marService.setStateChangeRunnable(this::refresh);
    }

    public void refresh() {
        MarState state = marService.getState();
        model.setRowCount(0);
        if (state == null) {
            return;
        }
        String basePath = project.getBasePath();
        state.getStateMap().entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey)).forEach(e -> {
            VirtualFile file = LocalFileSystem.getInstance().findFileByPath(e.getKey());
            if (file == null) {
                return;
            }
            if (!isSourceCode(file)) {
                return;
            }
            if (basePath == null) {
                return;
            }
            if (!e.getKey().startsWith(basePath)) {
                return;
            }
            String fileName = e.getKey().substring(basePath.length());

            FileState fileState = e.getValue();
            int totalLine = fileState.getLineCount();
            int readLine = fileState.getLineCountMap().getOrDefault(LineStatus.READ, 0);
            model.addRow(new String[] {fileName, readLine + "/" + totalLine});
        });
    }

    private boolean isSourceCode(VirtualFile file) {
        Module module = ProjectFileIndex.getInstance(project).getModuleForFile(file);
        if (module == null) {
            return false;
        }
        ModuleRootManager root = ModuleRootManager.getInstance(module);
        for (VirtualFile sourceRoot : root.getSourceRoots()) {
            if (file.getPath().startsWith(sourceRoot.getPath())) {
                return true;
            }
        }
        return false;
    }
}
