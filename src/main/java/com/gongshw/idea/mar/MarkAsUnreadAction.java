package com.gongshw.idea.mar;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * @author gongshiwei@baidu.com
 */
public class MarkAsUnreadAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Editor editor = e.getData(DataKeys.EDITOR);
        final VirtualFile file = e.getData(DataKeys.VIRTUAL_FILE);
        final Project project = e.getData(DataKeys.PROJECT);
        if (editor == null || project == null || file == null) {
            return;
        }
        final SelectionModel selectionModel = editor.getSelectionModel();
        if (selectionModel.getSelectionStartPosition() == null || selectionModel.getSelectionEndPosition() == null) {
            return;
        }
        final int startLine = selectionModel.getSelectionStartPosition().line;
        final int endLine = selectionModel.getSelectionEndPosition().line;
        MarService marService = MarService.getInstance(project);
        marService.markAsUnread(file.getPath(), startLine, endLine);

        Gutters.remove(editor, Gutters.x(startLine, endLine));
    }
}
