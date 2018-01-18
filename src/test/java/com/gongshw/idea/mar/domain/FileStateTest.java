package com.gongshw.idea.mar.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.Sets;

/**
 * @author gongshiwei@baidu.com
 */
public class FileStateTest {
    @Test
    public void test() {
        FileState fileState = new FileState();

        fileState.mark(0, 9, LineStatus.READ);
        assertEquals(1, fileState.getLineCountMap().size());
        assertEquals(10, fileState.getLineCountMap().get(LineStatus.READ).longValue());

        fileState.mark(5, 5, null);
        assertEquals(1, fileState.getLineCountMap().size());
        assertEquals(9, fileState.getLineCountMap().get(LineStatus.READ).longValue());
        assertEquals(9, fileState.getLines(LineStatus.READ).size());
        assertEquals(LineStatus.READ, fileState.getStatus(4));
        assertEquals(null, fileState.getStatus(5));
        assertEquals(LineStatus.READ, fileState.getStatus(6));

        fileState.mark(5, 5, null);
        assertEquals(1, fileState.getLineCountMap().size());
        assertEquals(9, fileState.getLineCountMap().get(LineStatus.READ).longValue());
        assertEquals(9, fileState.getLines(LineStatus.READ).size());
        assertEquals(LineStatus.READ, fileState.getStatus(4));
        assertEquals(null, fileState.getStatus(5));
        assertEquals(LineStatus.READ, fileState.getStatus(6));

        fileState.mark(0, 9, null);
        assertEquals(0, fileState.getLineCountMap().get(LineStatus.READ).longValue());
        assertEquals(0, fileState.getLines(LineStatus.READ).size());

        fileState.mark(1, 3, LineStatus.READ);
        fileState.mark(4, 6, LineStatus.READ);
        fileState.mark(7, 8, LineStatus.READ);
        assertEquals(Sets.newHashSet(1, 2, 3, 4, 5, 6, 7, 8), fileState.getLines(LineStatus.READ));
        fileState.mark(3, 7, null);
        assertEquals(Sets.newHashSet(1, 2, 8), fileState.getLines(LineStatus.READ));
        fileState.mark(4, 6, LineStatus.READ);
        assertEquals(Sets.newHashSet(1, 2, 4, 5, 6, 8), fileState.getLines(LineStatus.READ));
        fileState.mark(2, 8, LineStatus.READ);
        assertEquals(Sets.newHashSet(1, 2, 3, 4, 5, 6, 7, 8), fileState.getLines(LineStatus.READ));
    }
}
