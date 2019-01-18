package com.github.verils.transdoc.core.parser;

import com.github.verils.transdoc.core.model.*;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public abstract class WordParser implements Closeable {

    public static WordDocument parse(InputStream input) {
        try {
            return new DocParser(input).getDocument();
        } catch (IllegalArgumentException e) {
            return new DocxParser(input).getDocument();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract WordDocument getDocument();

    public abstract List<PictureEntry> getPictures();

    public abstract List<TableEntry> getTables();

    public abstract List<Entry> getEntries();
}
