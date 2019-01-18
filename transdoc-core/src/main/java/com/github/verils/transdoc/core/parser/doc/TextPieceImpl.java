package com.github.verils.transdoc.core.parser.doc;

import com.github.verils.transdoc.core.model.TextPiece;

public class TextPieceImpl implements TextPiece {

    private final String text;

    private final Style style;

    public TextPieceImpl(String text, Style style) {
        this.text = text;
        this.style = style;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public boolean isBold() {
        return style == Style.BOLD;
    }

    @Override
    public boolean isItalic() {
        return style == Style.ITALIC;
    }
}
