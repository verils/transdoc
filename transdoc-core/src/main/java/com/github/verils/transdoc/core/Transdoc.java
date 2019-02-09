package com.github.verils.transdoc.core;

import com.github.verils.transdoc.core.converter.Converter;
import com.github.verils.transdoc.core.model.WordDocument;
import com.github.verils.transdoc.core.parser.WordParser;
import com.github.verils.transdoc.core.util.Assert;

import java.io.*;
import java.nio.charset.Charset;

public class Transdoc {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    private final Converter converter;

    public Transdoc(Converter converter) {
        Assert.notNull("Converter required.", converter);
        this.converter = converter;
    }

    public void transform(InputStream input, OutputStream output) {
        transform(input, new OutputStreamWriter(output, UTF8));
    }

    public void transform(InputStream input, Writer writer) {
        Assert.notNull("Word document source required.", input);
        Assert.notNull("Output destination required.", writer);

        WordDocument wordDocument = WordParser.parseDocument(input);

        String content = converter.convert(wordDocument);

        PrintWriter printWriter = new PrintWriter(writer, true);
        printWriter.print(content);
        printWriter.print("\n");
        printWriter.flush();
        printWriter.close();
    }
}
