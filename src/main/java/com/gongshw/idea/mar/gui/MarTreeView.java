package com.gongshw.idea.mar.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.gongshw.idea.mar.MarService;
import com.gongshw.idea.mar.domain.FileState;
import com.gongshw.idea.mar.domain.LineStatus;
import com.gongshw.idea.mar.gui.util.HashNode;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author gongshiwei@baidu.com
 */
@Getter
public class MarTreeView {
    private JPanel content;
    private JTree tree;

    private final Project project;
    private final MarService marService;

    private MarTreeView(Project project) {
        this.project = project;
        marService = MarService.getInstance(project);
        marService.addStateChangeListener(this::refresh);
    }

    public static MarTreeView create(Project project) {
        MarTreeView marTreeView = new MarTreeView(project);
        marTreeView.init();
        marTreeView.refresh();
        return marTreeView;
    }

    private void init() {
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                TreePath treePath = tree.getPathForLocation(e.getX(), e.getY());
                if (treePath == null) {
                    return;
                }
                Object component = treePath.getLastPathComponent();
                if (!(component instanceof DefaultMutableTreeNode)) {
                    return;
                }
                if (((DefaultMutableTreeNode) component).getChildCount() > 0) {
                    return;
                }
                Object object = ((DefaultMutableTreeNode) component).getUserObject();
                if (!(object instanceof NamedFileStatus)) {
                    return;
                }
                String basePath = project.getBasePath();
                String fileName = basePath + ((NamedFileStatus) object).file;
                VirtualFile file = LocalFileSystem.getInstance().findFileByPath(fileName);
                if (file == null) {
                    return;
                }
                new OpenFileDescriptor(project, file).navigate(true);
            }
        });
    }

    private void refresh() {
        getTree().setModel(buildTreeModel());
    }

    private TreeModel buildTreeModel() {
        HashNode rootNode = new HashNode();
        marService.renderToUi((fileName, fileState) -> {
            rootNode.appendPath(fileName.substring(1), new NamedFileStatus(fileName, fileState));
        });
        return new DefaultTreeModel(rootNode.toTreeNode(project.getName()));
    }

    @RequiredArgsConstructor
    static class NamedFileStatus {
        private final String file;
        private final FileState fileState;

        @Override
        public String toString() {
            int readLine = fileState.getLines(LineStatus.READ).size();
            int totalLine = fileState.getLineCount();
            return file.substring(file.lastIndexOf("/") + 1) + "\t" + readLine + "/" + totalLine;
        }
    }
}
