package com.github.verils.transdoc.core.parser.doc;

import com.github.verils.transdoc.core.model.ParagraphPart;

public class ParagraphPartImpl implements ParagraphPart {

    private final String text;

    private final int titleLevel;

    public ParagraphPartImpl(String text, int titleLevel) {
        this.text = text;
        this.titleLevel = titleLevel;
    }

    @Override
    public boolean isTitle(int titleLevel) {
        return titleLevel > 0;
    }
}
