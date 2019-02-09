package com.github.verils.transdoc.core.parser;

import com.github.verils.transdoc.core.model.Part;
import com.github.verils.transdoc.core.model.PicturePart;
import com.github.verils.transdoc.core.model.TablePart;
import com.github.verils.transdoc.core.model.WordDocument;
import com.github.verils.transdoc.core.parser.doc.DocParser;
import com.github.verils.transdoc.core.parser.docx.DocxParser;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public abstract class WordParser implements Closeable {

    public static WordDocument parseDocument(InputStream input) {
        WordParser parser;
        try {
            parser = new DocParser(input);
        } catch (IllegalArgumentException e) {
            try {
                parser = new DocxParser(input);
            } catch (Exception ex) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return parser.parse();
    }

    public final WordDocument parse() {
        List<PicturePart> pictures = parsePictures();
        List<TablePart> tables = parseTables();
        List<Part> parts = parseParagraphs(pictures, tables);

        SimpleWordDocument simpleWordDocument = new SimpleWordDocument();
        simpleWordDocument.setPictures(pictures);
        simpleWordDocument.setTables(tables);
        simpleWordDocument.setParts(parts);
        return simpleWordDocument;
    }

    /**
     * 解析图片
     *
     * @return 图片列表
     */
    protected abstract List<PicturePart> parsePictures();

    /**
     * 解析表格
     *
     * @return 表格列表
     */
    protected abstract List<TablePart> parseTables();

    /**
     * 解析段落
     *
     * @param pictures 图片列表
     * @param tables   表格列表
     * @return 元素列表
     */
    protected abstract List<Part> parseParagraphs(List<PicturePart> pictures, List<TablePart> tables);
}
