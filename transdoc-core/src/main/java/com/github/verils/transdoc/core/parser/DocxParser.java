package com.github.verils.transdoc.core.parser;

import com.github.verils.transdoc.core.model.WordDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.IOException;
import java.io.InputStream;

class DocxParser extends WordParser {

    private final XWPFDocument xwpfDocument;

    DocxParser(InputStream input) throws IOException {
        this.xwpfDocument = new XWPFDocument(input);
    }

    @Override
    public WordDocument parse() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void close() throws IOException {
        xwpfDocument.close();
    }
}
