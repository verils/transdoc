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

    /**
     * 输出转换结果到二进制流，图片内容不进行输出存储
     *
     * @param input  Word文档输入流
     * @param output 转换后的文档输出流
     */
    public void transform(InputStream input, OutputStream output) {
        transform(input, new OutputStreamWriter(output, UTF8));
    }

    /**
     * 输出转换结果到字符流，图片内容不进行输出存储
     *
     * @param input  Word文档输入流
     * @param writer 转换后的文档输出流
     */
    public void transform(InputStream input, Writer writer) {
        Assert.notNull("Word document source required.", input);
        Assert.notNull("Output destination required.", writer);

        WordDocument wordDocument = WordParser.parseDocument(input);

        String content = converter.convert(wordDocument);

        writeContent(writer, content);
    }

    /**
     * 输出转换结果到文件，并且将图片内容存储为文件
     *
     * @param input Word文档输入流
     * @param file  转换后的文档输出文件
     * @throws IOException 文件不存在且无法创建时抛出该异常
     */
    public void transform(InputStream input, File file) throws IOException {
        Assert.notNull("Word document source required.", input);
        Assert.notNull("Output destination required.", file);

        file.mkdirs();

        FileWriter writer = new FileWriter(file);

        WordDocument wordDocument = WordParser.parseDocument(input);

        String content = converter.convert(wordDocument);
        converter.extractPictures(wordDocument, file);

        writeContent(writer, content);
    }

    private void writeContent(Writer writer, String content) {
        PrintWriter printWriter = new PrintWriter(writer, true);
        printWriter.print(content);
        printWriter.print("\n");
        printWriter.flush();
        printWriter.close();
    }
}
