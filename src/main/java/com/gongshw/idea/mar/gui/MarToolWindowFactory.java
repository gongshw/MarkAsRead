package com.gongshw.idea.mar.gui;

import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;

/**
 * @author gongshiwei@baidu.com
 */
public class MarToolWindowFactory implements ToolWindowFactory {
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();

        MarTableView marTableView = MarTableView.create(project);
        Content content = contentFactory.createContent(marTableView.getContent(), "Table View", false);
        toolWindow.getContentManager().addContent(content);

        MarTreeView marTreeView = MarTreeView.create(project);
        Content marTreeViewContent = contentFactory.createContent(marTreeView.getContent(), "Tree View", false);
        toolWindow.getContentManager().addContent(marTreeViewContent);
    }
}
