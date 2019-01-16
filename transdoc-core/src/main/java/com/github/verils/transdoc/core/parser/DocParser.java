package com.github.verils.transdoc.core.parser;

import com.github.verils.transdoc.core.model.*;
import com.github.verils.transdoc.core.parser.doc.*;
import com.github.verils.transdoc.core.util.StringUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.PicturesTable;
import org.apache.poi.hwpf.usermodel.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class DocParser extends WordParser {

    private final HWPFDocument document;

    private List<Part> parts;

    DocParser(InputStream source) throws IOException {
        this.document = new HWPFDocument(source);
        this.parts = parseDocument();
    }

    private List<Part> parseDocument() {
        List<PictureEntry> pictures = parsePictures();
        List<TableEntry> tables = parseTables();
        return parseDocumentParagraphs(pictures, tables);
    }

    private List<PictureEntry> parsePictures() {
        PicturesTable picturesTable = document.getPicturesTable();
        List<Picture> allPictures = picturesTable.getAllPictures();
        return IntStream.range(0, allPictures.size())
                .mapToObj(i -> createPictureEntry(i, allPictures.get(i)))
                .collect(Collectors.toList());
    }

    private List<TableEntry> parseTables() {
        TableIterator tableIterator = new TableIterator(document.getRange());
        List<TableEntry> tables = new ArrayList<>();
        while (tableIterator.hasNext()) {
            Table table = tableIterator.next();
            int rows = table.numRows();
            int cols = table.getRow(0).numCells();
            if (rows == 1 && cols == 1) {
                tables.add(createSingleCellTableEntry(table));
            } else {
                tables.add(createTableEntry(table));
            }
        }
        return tables;
    }

    private List<Part> parseDocumentParagraphs(List<PictureEntry> pictures, List<TableEntry> tables) {
        List<Part> parts = new ArrayList<>();
        Range documentRange = this.document.getRange();
        for (int i = 0; i < documentRange.numParagraphs(); i++) {
            Paragraph paragraph = documentRange.getParagraph(i);
            if (hasPicture(paragraph)) {
                getPicture(pictures, paragraph).ifPresent(parts::add);
            } else if (inTable(paragraph)) {
                getTable(tables, paragraph).ifPresent(parts::add);
            } else {
                getParagraph(paragraph).ifPresent(parts::add);
            }
        }
        return parts;
    }

    private Optional<PictureEntry> getPicture(List<PictureEntry> pictures, Paragraph paragraph) {
        return Optional.empty();
    }

    private Optional<TableEntry> getTable(List<TableEntry> tables, Paragraph paragraph) {
        return Optional.empty();
    }

    private Optional<Part> getParagraph(Paragraph paragraph) {
        String text = paragraph.text();
        if (StringUtils.hasText(text)) {
            int titleLevel = getTitleLevel(paragraph.getLvl());
            if (isTitle(titleLevel)) {
                TitleParagraphImpl titleParagraph = new TitleParagraphImpl(text, titleLevel);
                return Optional.of(titleParagraph);
            } else {
                TextParagraphImpl textParagraph = new TextParagraphImpl();
                return Optional.of(textParagraph);
            }
        }
        return Optional.empty();
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

    private SingleCellTableEntry createSingleCellTableEntry(Table table) {
        TableCell cell = table.getRow(0).getCell(0);
        List<Part> parts = IntStream.range(0, cell.numCharacterRuns())
                .mapToObj(cell::getParagraph)
                .map(this::getParagraph)
                .map(Optional::get)
                .collect(Collectors.toList());
        return new SingleCellTableEntryImpl(parts);
    }

    private SingleCellTableEntry createTableEntry(Table table) {
        return null;
    }

    private boolean hasPicture(Paragraph paragraph) {
        return false;
    }

    private boolean inTable(Paragraph paragraph) {
        return false;
    }

    private int getTitleLevel(int level) {
        boolean isLevelSupported = level >= 0 && level < 6;
        return isLevelSupported ? level + 1 : 0;
    }

    private boolean isTitle(int titleLevel) {
        return titleLevel > 0;
    }

    @Override
    public Article getArticle() {
        return new ArticleImpl(parts);
    }

    @Override
    public void close() throws IOException {
        document.close();
    }
}
