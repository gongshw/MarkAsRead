package com.gongshw.idea.mar.domain;

import javax.swing.Icon;

import com.gongshw.idea.mar.Icons;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author gongshiwei@baidu.com
 */
@Getter
@RequiredArgsConstructor
public enum LineStatus {
    READ(Icons.READ);
    private final Icon icon;
}
