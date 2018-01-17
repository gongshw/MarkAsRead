package com.gongshw.idea.mar.domain;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.val;

@Data
public class FileState {
    private Map<LineRange, LineStatus> statusMap = new TreeMap<>();

    public Optional<LineStatus> getStatus(int line) {
        return statusMap.entrySet().stream()
                .filter(e -> e.getKey().contains(line))
                .map(Map.Entry::getValue)
                .findFirst();
    }

    public Set<Integer> getLines(LineStatus status) {
        return statusMap.entrySet().stream()
                .filter(e -> Objects.equals(e.getValue(), status))
                .map(Map.Entry::getKey)
                .map(LineRange::toLines)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    public void mark(int start, int end, LineStatus lineStatus) {
        val wrapped = statusMap.entrySet().stream()
                .filter(e -> e.getKey().getStart() <= start && e.getKey().getEnd() >= end)
                .collect(Collectors.toSet());
        wrapped.forEach(e -> statusMap.remove(e.getKey()));

        AtomicInteger newStart = new AtomicInteger(start);
        statusMap.entrySet().stream().filter(e -> e.getKey().contains(start)).findAny().ifPresent(existStart -> {
            LineRange range = existStart.getKey();
            LineStatus status = existStart.getValue();
            statusMap.remove(range);
            if (status == lineStatus) {
                newStart.set(range.getStart());
            } else {
                statusMap.put(LineRange.of(range.getStart(), start - 1), status);
            }
        });

        AtomicInteger newEnd = new AtomicInteger(end);
        statusMap.entrySet().stream().filter(e -> e.getKey().contains(end)).findAny().ifPresent(existEnd -> {
            LineRange range = existEnd.getKey();
            LineStatus status = existEnd.getValue();
            statusMap.remove(range);
            if (status == lineStatus) {
                newEnd.set(range.getEnd());
            } else {
                statusMap.put(LineRange.of(range.getStart(), end + 1), status);
            }
        });

        if (lineStatus != null) {
            statusMap.put(LineRange.of(newStart.get(), newEnd.get()), lineStatus);
        }
    }
}

