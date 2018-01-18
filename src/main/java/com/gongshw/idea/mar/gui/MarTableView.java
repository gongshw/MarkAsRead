package com.gongshw.idea.mar.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.gongshw.idea.mar.MarService;
import com.gongshw.idea.mar.domain.LineStatus;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

import lombok.Getter;

/**
 * @author gongshiwei@baidu.com
 */
@Getter
public class MarTableView {
    private JPanel content;
    private JTable table;

    private Project project;
    private DefaultTableModel model;
    private MarService marService;

    public static MarTableView create(Project project) {
        MarTableView marTableView = new MarTableView(project);
        marTableView.init();
        marTableView.refresh();
        return marTableView;
    }

    private MarTableView(Project project) {
        this.project = project;
    }

    private void init() {
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
        marService.addStateChangeListener(this::refresh);
    }

    private void refresh() {
        model.setRowCount(0);
        marService.renderToUi((fileName, fileState) -> {
            int totalLine = fileState.getLineCount();
            int readLine = fileState.getLineCountMap().getOrDefault(LineStatus.READ, 0);
            model.addRow(new String[] {fileName, readLine + "/" + totalLine});
        });
    }
}
