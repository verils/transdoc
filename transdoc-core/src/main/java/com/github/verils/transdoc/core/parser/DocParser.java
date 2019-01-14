package com.github.verils.transdoc.core;

import com.github.verils.transdoc.core.model.Article;

import java.io.InputStream;

public class DocParser extends WordParser {

    protected DocParser(InputStream source) {
        super(source);
    }

    @Override
    public Article getArticle() {
        return null;
    }
}
