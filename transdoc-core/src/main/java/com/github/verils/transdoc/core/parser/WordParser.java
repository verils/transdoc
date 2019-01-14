package com.github.verils.transdoc.core.parser;

import com.github.verils.transdoc.core.old.model.Article;

import java.io.IOException;
import java.io.InputStream;

public abstract class WordParser {

    public static WordParser parse(InputStream input) throws IOException {
        try {
            return new DocParser(input);
        } catch (IllegalArgumentException e) {
            return new DocxParser(input);
        }
    }

    public abstract Article getArticle();
}
