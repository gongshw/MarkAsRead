package com.gongshw.idea.mar;

import javax.swing.JPanel;

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
        MarToolWindow window = new MarToolWindow(project);
        window.init();

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        MarService marService = MarService.getInstance(project);
        marService.setStateChangeRunnable(window::refresh);
        window.refresh();

        JPanel windowContent = window.getContent();
        Content content = contentFactory.createContent(windowContent, "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
