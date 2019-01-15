package com.github.verils.transdoc.core.parser;

import com.github.verils.transdoc.core.model.Article;

import java.io.IOException;
import java.io.InputStream;

class DocxParser extends WordParser {

    DocxParser(InputStream source) {
    }

    @Override
    public Article getArticle() {
        return null;
    }

    @Override
    public void close() throws IOException {

    }
}
