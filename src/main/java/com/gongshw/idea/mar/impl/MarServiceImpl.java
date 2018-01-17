package com.gongshw.idea.mar.impl;

import org.jetbrains.annotations.NotNull;

import com.gongshw.idea.mar.MarService;
import com.gongshw.idea.mar.domain.FileState;
import com.gongshw.idea.mar.domain.LineStatus;
import com.gongshw.idea.mar.domain.MarState;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;

@State(name = "MarService", storages = @Storage("MarService.xml"))
public class MarServiceImpl implements MarService {

    private Logger logger = Logger.getInstance(getClass());

    private MarState marState = new MarState();

    public MarServiceImpl(Project project) {
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
    }

    @Override
    public void markAsUnread(String file, int lineStart, int lineEnd) {
        FileState fileState = marState.getStateMap().getOrDefault(file, new FileState());
        fileState.mark(lineStart, lineEnd, null);
        marState.getStateMap().put(file, fileState);
        logger.info(String.format("mark as unread, l%d-%d, %s", lineStart, lineEnd, file));
    }

    @Override
    public boolean isRead(String file, int line) {
        return marState.getStateMap().containsKey(file)
                && marState.getStateMap().get(file).getStatus(line).orElse(null) == LineStatus.READ;
    }
}
