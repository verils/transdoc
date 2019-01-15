package com.github.verils.transdoc.core;

import com.github.verils.transdoc.core.model.Article;
import com.github.verils.transdoc.core.parser.WordParser;
import com.github.verils.transdoc.core.util.Assert;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Transdoc {

    public static void parse(Converter converter, InputStream input, OutputStream output) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(output, StandardCharsets.UTF_8);
        parse(converter, input, writer);
    }

    public static void parse(Converter converter, InputStream input, Writer writer) throws IOException {
        Assert.notNull("Converter required.", converter);
        Assert.notNull("Word document source required.", input);
        Assert.notNull("Output destination required.", writer);

        WordParser parser = WordParser.parse(input);
        Article article = parser.getArticle();

        String formattedContent = converter.convert(article);

        PrintWriter printWriter = new PrintWriter(writer, true);
        printWriter.println(formattedContent);
    }
}
