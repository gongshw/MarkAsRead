package com.gongshw.idea.mar;

import com.intellij.openapi.editor.markup.GutterIconRenderer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Objects;

public final class MyGutterIconRenderer extends GutterIconRenderer {

    private int line;

    public MyGutterIconRenderer(int line) {
        this.line = line;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof MyGutterIconRenderer;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this);
    }

    @NotNull
    @Override
    public Icon getIcon() {
        return Icons.READ;
    }

    public int getLine() {
        return line;
    }
}
