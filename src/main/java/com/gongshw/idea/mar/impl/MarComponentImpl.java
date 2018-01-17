package com.gongshw.idea.mar.impl;

import com.gongshw.idea.mar.FileState;
import com.gongshw.idea.mar.Gutters;
import com.gongshw.idea.mar.MarComponent;
import com.gongshw.idea.mar.MarService;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.messages.MessageBus;
import org.jetbrains.annotations.NotNull;

public class MarComponentImpl implements MarComponent {

    private Logger logger = Logger.getInstance(getClass());

    private final Project project;

    MarComponentImpl(Project project) {
        this.project = project;
        MessageBus messageBus = project.getMessageBus();
        messageBus.connect().subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, new FileEditorManagerListener() {
            @Override
            public void fileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
                @NotNull final MarService marService = MarService.getInstance(project);
                final Editor editor = source.getSelectedTextEditor();
                final FileState fileState = marService.getState().stateMap.get(file.getPath());
                if (editor == null || fileState == null) {
                    return;
                }
                Gutters.add(editor, fileState.lines);
            }

            @Override
            public void fileClosed(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
                logger.info("fileClosed");
            }

            @Override
            public void selectionChanged(@NotNull FileEditorManagerEvent event) {
                logger.info("selectionChanged");
            }
        });
    }

    @Override
    public void projectOpened() {

    }

    @Override
    public void projectClosed() {

    }

    @Override
    public void initComponent() {

    }

    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
        return getClass().getName();
    }
}
