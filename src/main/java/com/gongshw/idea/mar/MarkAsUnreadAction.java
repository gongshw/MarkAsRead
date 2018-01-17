package com.gongshw.idea.mar;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * @author gongshiwei@baidu.com
 */
public class MarkAsUnreadAction extends BaseLineSelectAction {
    @Override
    void onSelectAction(Project project, VirtualFile file, Editor editor, int startLine, int endLine) {
        MarService marService = MarService.getInstance(project);
        marService.markAsUnread(file.getPath(), startLine, endLine);
        Gutters.remove(editor, Gutters.x(startLine, endLine));
    }
}
