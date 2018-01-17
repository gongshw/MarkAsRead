package com.gongshw.idea.mar.domain;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;

import lombok.Data;

/**
 * @author gongshiwei@baidu.com
 */
@Data
public class LineRange implements Comparable<LineRange> {
    private int start;
    private int end;

    @Override
    public int compareTo(@NotNull LineRange o) {
        return this.start - o.start;
    }

    public boolean contains(int line) {
        return start <= line && line <= end;
    }

    public Set<Integer> toLines() {
        return Stream.iterate(start, i -> i + 1).limit(end - start + 1).collect(Collectors.toSet());
    }

    public static LineRange of(int start, int end) {
        LineRange lineRange = new LineRange();
        lineRange.setStart(start);
        lineRange.setEnd(end);
        return lineRange;
    }

    public static LineRange of(int line) {
        return of(line, line);
    }
}
