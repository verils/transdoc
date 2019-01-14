package com.github.verils.transdoc.core;

import com.github.verils.transdoc.core.model.Article;

import java.io.InputStream;

public abstract class WordParser {

    public static WordParser parse(InputStream input) {
        return null;
    }

    private final InputStream source;

    WordParser(InputStream source) {
        this.source = source;
    }

    public abstract Article getArticle();
}
