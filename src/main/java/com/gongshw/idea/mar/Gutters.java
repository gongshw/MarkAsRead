package com.gongshw.idea.mar;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.MarkupModel;
import com.intellij.openapi.editor.markup.RangeHighlighter;

public class Gutters {

    public static void add(Editor editor, Set<Integer> lines) {
        remove(editor, lines);
        int lineCount = editor.getDocument().getLineCount();
        lines.stream().filter(l -> l < lineCount).forEach(i -> {
            MyGutterIconRenderer myGutterIconRenderer = new MyGutterIconRenderer(i);
            MarkupModel editorMarkupModel = editor.getMarkupModel();
            RangeHighlighter rangeHighlighter = editorMarkupModel.addLineHighlighter(i, HighlighterLayer.FIRST, null);
            rangeHighlighter.setGutterIconRenderer(myGutterIconRenderer);
        });
    }

    public static void remove(Editor editor, Set<Integer> lines) {
        for (RangeHighlighter highlighter : editor.getMarkupModel().getAllHighlighters()) {
            GutterIconRenderer iconRenderer = highlighter.getGutterIconRenderer();
            if (iconRenderer instanceof MyGutterIconRenderer) {
                int line = ((MyGutterIconRenderer) iconRenderer).getLine();
                if (lines.contains(line)) {
                    editor.getMarkupModel().removeHighlighter(highlighter);
                }
            }
        }
    }

    public static Set<Integer> x(int start, int end) {
        return Stream.iterate(start, i -> i + 1).limit(end - start + 1).collect(Collectors.toSet());
    }
}
