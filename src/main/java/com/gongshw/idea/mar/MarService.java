package com.gongshw.idea.mar;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleServiceManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public interface MarService extends PersistentStateComponent<MarState> {
    static MarService getInstance(Project project) {
        return ServiceManager.getService(project, MarService.class);
    }

    void markAsRead(String file, int lineStart, int lineEnd);

    void markAsUnread(String file, int lineStart, int lineEnd);

    boolean isRead(String file, int line);
}
