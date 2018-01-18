package com.gongshw.idea.mar;

import java.util.function.BiConsumer;

import org.jetbrains.annotations.NonNls;

import com.gongshw.idea.mar.domain.FileState;
import com.gongshw.idea.mar.domain.MarState;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;

public interface MarService extends PersistentStateComponent<MarState> {
    static MarService getInstance(Project project) {
        return ServiceManager.getService(project, MarService.class);
    }

    void markAsRead(String file, int lineStart, int lineEnd);

    void markAsUnread(String file, int lineStart, int lineEnd);

    void updateFileStatus(String file, int lineCount);

    FileState getFileState(String file);

    void addStateChangeListener(@NonNls Runnable runnable);

    void renderToUi(BiConsumer<String, FileState> consumer);
}
