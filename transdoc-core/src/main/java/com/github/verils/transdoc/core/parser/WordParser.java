package com.github.verils.transdoc.core.parser;

import com.github.verils.transdoc.core.model.Article;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public abstract class WordParser implements Closeable {

    public static WordParser parse(InputStream input) {
        try {
            return new DocParser(input);
        } catch (IllegalArgumentException e) {
            return new DocxParser(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract Article getArticle();
}
