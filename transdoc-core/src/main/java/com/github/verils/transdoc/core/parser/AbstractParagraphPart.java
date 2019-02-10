package com.github.verils.transdoc.core.parser;

import com.github.verils.transdoc.core.model.ParagraphPart;
import com.github.verils.transdoc.core.model.TextPiece;
import com.github.verils.transdoc.core.parser.doc.DocTextPiece;

import java.util.Collections;
import java.util.List;

public abstract class AbstractParagraphPart implements ParagraphPart {

    private final List<TextPiece> textPieces;
    private final int titleLevel;

    /**
     * For title paragraph
     *
     * @param text       文本内容
     * @param titleLevel 标题大纲级别
     */
    public AbstractParagraphPart(String text, int titleLevel) {
        TextPiece textPiece = new DocTextPiece(text, TextPiece.Style.NONE);
        this.textPieces = Collections.singletonList(textPiece);
        this.titleLevel = titleLevel;
    }

    /**
     * For text paragraph
     *
     * @param textPieces 文本内容
     */
    public AbstractParagraphPart(List<TextPiece> textPieces) {
        this.textPieces = textPieces;
        this.titleLevel = 0;
    }

    @Override
    public boolean isTitle() {
        return titleLevel > 0;
    }

    @Override
    public int getTitleLevel() {
        return 0;
    }

    @Override
    public String getText() {
        StringBuilder builder = new StringBuilder();
        for (TextPiece textPiece : textPieces) {
            builder.append(textPiece.getText());
        }
        return builder.toString();
    }

    @Override
    public List<TextPiece> getTextPieces() {
        return textPieces;
    }

    @Override
    public String toString() {
        return "AbstractParagraphPart{\"" + getText() + "\"}";
    }
}
