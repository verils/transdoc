package com.github.verils.transdoc.core.parser;

import com.github.verils.transdoc.core.model.WordDocument;

import java.io.IOException;
import java.io.InputStream;

public abstract class WordParser {

    public static WordDocument parseDocument(InputStream input) {
        try {
            return new DocParser().parse(input);
        } catch (IllegalArgumentException e) {
            try {
                return new DocxParser().parse(input);
            } catch (Exception ex) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract WordDocument parse(InputStream source) throws IOException;
}
