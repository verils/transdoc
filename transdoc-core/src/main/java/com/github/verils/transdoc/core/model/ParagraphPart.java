package com.github.verils.transdoc.core.model;


import java.util.List;

public interface ParagraphPart extends Part {

    boolean isTitle();

    int getTitleLevel();

    String getText();

    List<TextPiece> getTextPieces();
}
