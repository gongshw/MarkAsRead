package com.gongshw.idea.mar.impl;

import com.gongshw.idea.mar.FileState;
import com.gongshw.idea.mar.MarService;
import com.gongshw.idea.mar.MarState;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

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
        FileState fileState = marState.stateMap.getOrDefault(file, new FileState());
        for (int i = lineStart; i <= lineEnd; i++) {
            fileState.lines.add(i);
        }
        marState.stateMap.put(file, fileState);
        logger.info(String.format("mark as read, l%d-%d, %s", lineStart, lineEnd, file));
    }

    @Override
    public void markAsUnread(String file, int lineStart, int lineEnd) {
        FileState fileState = marState.stateMap.getOrDefault(file, new FileState());
        for (int i = lineStart; i <= lineEnd; i++) {
            fileState.lines.remove(i);
        }
        marState.stateMap.put(file, fileState);
        logger.info(String.format("mark as unread, l%d-%d, %s", lineStart, lineEnd, file));
    }

    @Override
    public boolean isRead(String file, int line) {
        return marState.stateMap.containsKey(file) && marState.stateMap.get(file).lines.contains(line);
    }
}
