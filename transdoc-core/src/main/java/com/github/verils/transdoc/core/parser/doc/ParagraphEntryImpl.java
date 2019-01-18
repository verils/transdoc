package com.github.verils.transdoc.core.parser.doc;

import com.github.verils.transdoc.core.model.ParagraphEntry;

public class ParagraphEntryImpl implements ParagraphEntry {

    private final String text;

    private final int titleLevel;

    public ParagraphEntryImpl(String text, int titleLevel) {
        this.text = text;
        this.titleLevel = titleLevel;
    }

    @Override
    public boolean isTitle(int titleLevel) {
        return titleLevel > 0;
    }
}
