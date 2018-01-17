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
public abstract class BaseLineSelectAction extends AnAction {
    @Override
    public final void actionPerformed(AnActionEvent e) {

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
        final int startLine = editor.visualToLogicalPosition(selectionModel.getSelectionStartPosition()).line;
        final int endLine = editor.visualToLogicalPosition(selectionModel.getSelectionEndPosition()).line;

        onSelectAction(project, file, editor, startLine, endLine);

    }

    abstract void onSelectAction(Project project, VirtualFile file, Editor editor, int startLine, int endLine);
}
