package com.gongshw.idea.mar.domain;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Data;
import lombok.val;

@Data
public class FileState {
    private Map<LineRange, LineStatus> statusMap = new TreeMap<>();
    private int lineCount;

    public LineStatus getStatus(int line) {
        return statusMap.entrySet().stream()
                .filter(e -> e.getKey().contains(line))
                .map(Map.Entry::getValue)
                .findAny().orElse(null);
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

        Set<LineRange> rangeToRemove = new HashSet<>();

        val wrapped = statusMap.entrySet().stream()
                .filter(e -> start <= e.getKey().getStart() && e.getKey().getEnd() <= end)
                .collect(Collectors.toSet());
        wrapped.forEach(e -> statusMap.remove(e.getKey()));

        AtomicInteger newStart = new AtomicInteger(start);
        statusMap.entrySet().stream().filter(e -> e.getKey().contains(start)).findAny().ifPresent(existStart -> {
            LineRange range = existStart.getKey();
            LineStatus status = existStart.getValue();
            rangeToRemove.add(range);
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
            rangeToRemove.add(range);
            if (status == lineStatus) {
                newEnd.set(range.getEnd());
            } else {
                statusMap.put(LineRange.of(end + 1, range.getEnd()), status);
            }
        });

        if (lineStatus != null) {
            statusMap.put(LineRange.of(newStart.get(), newEnd.get()), lineStatus);
        }

        rangeToRemove.forEach(statusMap::remove);
    }

    public Map<LineStatus, Integer> getLineCountMap() {
        Map<LineStatus, Integer> result = statusMap.entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue))
                .entrySet().stream().collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e0 -> e0.getValue().stream().mapToInt(e1 -> e1.getKey().lineCount()).sum()
                ));
        Stream.of(LineStatus.values()).forEach(s -> result.putIfAbsent(s, 0));
        return result;
    }
}
