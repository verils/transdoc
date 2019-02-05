package com.github.verils.transdoc.core.parser;

import com.github.verils.transdoc.core.model.WordDocument;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public abstract class WordParser implements Closeable {

    public static WordDocument parseDocument(InputStream input) {
        try {
            return new DocParser(input).parse();
        } catch (IllegalArgumentException e) {
            try {
                return new DocxParser(input).parse();
            } catch (Exception ex) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    abstract WordDocument parse();
}
