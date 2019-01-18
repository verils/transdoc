package com.github.verils.transdoc.core.model;

public interface TextPiece {

    enum Style {
        BOLD, ITALIC, NONE
    }

    String getText();

    boolean isBold();

    boolean isItalic();
}
