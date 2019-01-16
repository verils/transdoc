package com.github.verils.transdoc.core.parser.doc;

import com.github.verils.transdoc.core.model.TitleParagraph;

public class TitleParagraphImpl implements TitleParagraph {

    private final String text;

    private final int level;

    public TitleParagraphImpl(String text, int level) {
        this.text = text;
        this.level = level;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public int getLevel() {
        return level;
    }
}
