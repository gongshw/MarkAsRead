package com.gongshw.idea.mar.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

import com.gongshw.idea.mar.domain.FileState;

@Data
public class MarState {
    private Map<String, FileState> stateMap = new HashMap<>();
}
