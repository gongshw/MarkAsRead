package com.gongshw.idea.mar.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.jetbrains.annotations.NotNull;

import com.gongshw.idea.mar.MarService;
import com.gongshw.idea.mar.domain.FileState;
import com.gongshw.idea.mar.domain.LineStatus;
import com.gongshw.idea.mar.domain.MarState;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.xmlb.XmlSerializerUtil;

import lombok.Setter;

@State(name = "MarService", storages = @Storage("MarService.xml"))
public class MarServiceImpl implements MarService {

    private final Project project;
    private Logger logger = Logger.getInstance(getClass());

    private MarState marState = new MarState();

    @Setter
    private List<Runnable> stateChangeListeners = new ArrayList<>();

    public MarServiceImpl(Project project) {
        this.project = project;
    }

    @NotNull
    @Override
    public MarState getState() {
        return marState;
    }

    @Override
    public void loadState(MarState marState) {
        XmlSerializerUtil.copyBean(marState, this.marState);
    }

    @Override
    public void markAsRead(String file, int lineStart, int lineEnd) {
        FileState fileState = marState.getStateMap().getOrDefault(file, new FileState());
        fileState.mark(lineStart, lineEnd, LineStatus.READ);
        marState.getStateMap().put(file, fileState);
        logger.info(String.format("mark as read, l%d-%d, %s", lineStart, lineEnd, file));
        triggerChange();
    }

    @Override
    public void markAsUnread(String file, int lineStart, int lineEnd) {
        FileState fileState = marState.getStateMap().getOrDefault(file, new FileState());
        fileState.mark(lineStart, lineEnd, null);
        marState.getStateMap().put(file, fileState);
        logger.info(String.format("mark as unread, l%d-%d, %s", lineStart, lineEnd, file));
        triggerChange();
    }

    @Override
    public void updateFileStatus(String file, int lineCount) {
        FileState fileState = getFileState(file);
        fileState.setLineCount(lineCount);
    }

    @Override
    public FileState getFileState(String file) {
        if (marState.getStateMap().containsKey(file)) {
            return marState.getStateMap().get(file);
        } else {
            FileState fileState = new FileState();
            marState.getStateMap().put(file, fileState);
            triggerChange();
            return fileState;
        }
    }

    @Override
    public void addStateChangeListener(Runnable runnable) {
        stateChangeListeners.add(runnable);
    }

    @Override
    public void renderToUi(BiConsumer<String, FileState> consumer) {
        String basePath = project.getBasePath();
        getState().getStateMap().entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey)).forEach(e -> {
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
            consumer.accept(fileName, fileState);
        });
    }

    private void triggerChange() {
        stateChangeListeners.forEach(Runnable::run);
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
