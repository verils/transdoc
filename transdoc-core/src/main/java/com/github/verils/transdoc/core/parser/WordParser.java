package com.github.verils.transdoc.core.parser;

import com.github.verils.transdoc.core.model.WordDocument;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public abstract class WordParser {

    public static WordDocument parse(InputStream input) {
        try {
            return new DocParser(input).parse();
        } catch (IllegalArgumentException e) {
            return new DocxParser(input).parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract WordDocument parse() throws IOException;
}
