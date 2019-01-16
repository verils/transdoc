package com.github.verils.transdoc.core.parser;

import com.github.verils.transdoc.core.model.*;
import com.github.verils.transdoc.core.util.StringUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.PicturesTable;
import org.apache.poi.hwpf.usermodel.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class DocParser extends WordParser {

    private final HWPFDocument document;

    private List<PictureEntry> pictures;

    private List<TablePart> tables;

    DocParser(InputStream source) throws IOException {
        this.document = new HWPFDocument(source);
        parse();
    }

    private void parse() {
        parsePictures();
        parseTables();
        parseParagraphs();
    }

    private void parsePictures() {
        PicturesTable picturesTable = document.getPicturesTable();
        List<Picture> allPictures = picturesTable.getAllPictures();
        this.pictures = IntStream.range(0, allPictures.size())
                .mapToObj(i -> createPictureEntry(i, allPictures.get(i)))
                .collect(Collectors.toList());
    }

    private PictureEntry createPictureEntry(int index, Picture picture) {
        PictureEntryImpl entry = new PictureEntryImpl();
        entry.setId(index);
        entry.setName(picture.suggestFullFileName());
        entry.setExtension(picture.suggestFileExtension());
        entry.setDataOffset(picture.getStartOffset());
        entry.setData(picture.getContent());
        return entry;
    }

    private void parseTables() {
        TableIterator tableIterator = new TableIterator(document.getRange());
        while (tableIterator.hasNext()) {
            Table table = tableIterator.next();
            int rows = table.numRows();
            int cols = table.getRow(0).numCells();
            if (rows == 1 && cols == 1) {
                TableCell cell = table.getRow(0).getCell(0);
                String content = parseCellContent(cell);
                SimpleTableEntry simpleTableEntry = new SimpleTableEntry();
                simpleTableEntry.setContent(content);
            } else {
                TableEntry tableEntry = new TableEntry();

            }
        }
    }

    private String parseCellContent(TableCell cell) {
        return null;
    }

    private void parseParagraphs() {
        List<Part> parts = new ArrayList<>();
        Range documentRange = this.document.getRange();
        int numParagraphs = documentRange.numParagraphs();
        for (int i = 0; i < numParagraphs; i++) {
            Paragraph paragraph = documentRange.getParagraph(i);
            if (hasPicture(paragraph)) {

            } else if (inTable(paragraph)) {

            } else {
                parseParagraph(parts, paragraph);
            }
        }
    }

    @Override
    public Article getArticle() {
        Article article = new ArticleImpl();
        return article;
    }

    private boolean hasPicture(Paragraph paragraph) {
        return false;
    }

    private boolean inTable(Paragraph paragraph) {
        return false;
    }

    private void parseParagraph(List<Part> parts, Paragraph paragraph) {
        String text = paragraph.text();
        if (StringUtils.hasText(text)) {
            int titleLevel = getTitleLevel(paragraph.getLvl());
            if (isTitle(titleLevel)) {
                TitleParagraph titleParagraph = new TitleParagraph();
            } else {
                TextParagraph textParagraph = new TextParagraph();
            }
        }
    }

    private int getTitleLevel(int level) {
        boolean isLevelSupported = level >= 0 && level < 6;
        return isLevelSupported ? level + 1 : 0;
    }

    private boolean isTitle(int titleLevel) {
        return titleLevel > 0;
    }

    @Override
    public void close() throws IOException {
        document.close();
    }
}
