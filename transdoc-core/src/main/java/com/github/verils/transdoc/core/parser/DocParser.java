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

class DocParser extends WordParser {

    private final InputStream source;

    DocParser(InputStream source) {
        this.source = source;
    }

    @Override
    public WordDocument parse() throws IOException {
        HWPFDocument hwpfDocument = new HWPFDocument(source);

        List<PictureEntry> pictureEntries = parsePictures(hwpfDocument);
        List<TableEntry> tableEntries = parseTables(hwpfDocument);
        List<Entry> entries = parseParagraphs(hwpfDocument, pictureEntries, tableEntries);

        hwpfDocument.close();

        WordDocumentImpl docWordDocument = new WordDocumentImpl();
        docWordDocument.setPictures(pictureEntries);
        docWordDocument.setTables(tableEntries);
        docWordDocument.setEntries(entries);
        return docWordDocument;
    }

    /**
     * 解析图片
     *
     * @param hwpfDocument hwpfDocument
     * @return 图片列表
     */
    private List<PictureEntry> parsePictures(HWPFDocument hwpfDocument) {
        List<PictureEntry> pictureEntries = new ArrayList<PictureEntry>();
        PicturesTable picturesTable = hwpfDocument.getPicturesTable();
        List<Picture> allPictures = picturesTable.getAllPictures();
        for (int i = 0; i < allPictures.size(); i++) {
            Picture picture = allPictures.get(i);
            pictureEntries.add(createPictureEntry(i, picture));
        }
        return pictureEntries;
    }

    /**
     * 解析表格
     *
     * @param hwpfDocument hwpfDocument
     * @return 表格列表
     */
    private List<TableEntry> parseTables(HWPFDocument hwpfDocument) {
        List<TableEntry> tables = new ArrayList<TableEntry>();
        TableIterator tableIterator = new TableIterator(hwpfDocument.getRange());
        while (tableIterator.hasNext()) {
            Table table = tableIterator.next();
            tables.add(createTableEntry(table));
        }
        return tables;
    }

    /**
     * 解析段落
     *
     * @param hwpfDocument hwpfDocument
     * @param pictures     图片列表
     * @param tables       表格列表
     * @return 元素列表
     */
    private List<Entry> parseParagraphs(HWPFDocument hwpfDocument, List<PictureEntry> pictures, List<TableEntry> tables) {
        List<Entry> entries = new ArrayList<Entry>();
        PicturesTable picturesTable = hwpfDocument.getPicturesTable();
        Range documentRange = hwpfDocument.getRange();
        int pictureIndex = 0, tableIndex = 0;
        for (int i = 0; i < documentRange.numParagraphs(); i++) {
            Paragraph paragraph = documentRange.getParagraph(i);
            if (hasPicture(picturesTable, paragraph)) {
                PictureEntry pictureEntry = pictures.get(pictureIndex++);
                entries.add(pictureEntry);
            } else if (inTable(tables, paragraph)) {
                TableEntry tableEntry = tables.get(tableIndex++);
                entries.add(tableEntry);
            } else {
                ParagraphEntry paragraphEntry = createParagraphEntry(paragraph);
                entries.add(paragraphEntry);
            }
        }
        return entries;
    }

    private PictureEntry createPictureEntry(int index, Picture picture) {
        PictureEntryImpl pictureEntry = new PictureEntryImpl();
        pictureEntry.setId(index);
        pictureEntry.setName(picture.suggestFullFileName());
        pictureEntry.setExtension(picture.suggestFileExtension());
        pictureEntry.setDataOffset(picture.getStartOffset());
        pictureEntry.setData(picture.getContent());
        return pictureEntry;
    }

    private TableEntry createTableEntry(Table table) {
        int rows = table.numRows();
        int cols = table.getRow(0).numCells();
        TableEntryImpl tableEntry = new TableEntryImpl(table.getStartOffset(), table.getEndOffset(), rows, cols);
        for (int i = 0; i < rows; i++) {
            TableRow row = table.getRow(i);
            for (int j = 0; j < row.numCells(); j++) {
                TableCell cell = row.getCell(j);
                TableCellEntry tableCellEntry = createTableCellEntry(cell);
                tableEntry.setCell(i, j, tableCellEntry);
            }
        }
        return tableEntry;
    }

    private TableCellEntry createTableCellEntry(TableCell cell) {
        List<Entry> entries = new ArrayList<Entry>();
        for (int i = 0; i < cell.numParagraphs(); i++) {
            Paragraph paragraph = cell.getParagraph(i);
            ParagraphEntry entry = createParagraphEntry(paragraph);
            entries.add(entry);
        }
        return new TableCellEntryImpl(entries);
    }

    private ParagraphEntry createParagraphEntry(Paragraph paragraph) {
        String text = escapeText(paragraph.text());
        if (StringUtils.hasText(text)) {
            int titleLevel = getTitleLevel(paragraph.getLvl());
            return new ParagraphEntryImpl(text, titleLevel);
        }
        return new ParagraphEntryImpl("", 0);
    }

    private String escapeText(String text) {
        return text.replaceAll("\r", "\n");
    }

    private boolean hasPicture(PicturesTable picturesTable, Paragraph paragraph) {
        for (int i = 0; i < paragraph.numCharacterRuns(); i++) {
            CharacterRun characterRun = paragraph.getCharacterRun(i);
            if (picturesTable.hasPicture(characterRun)) {
                return true;
            }
        }
        return false;
    }

    private boolean inTable(List<TableEntry> tables, Paragraph paragraph) {
        if (!paragraph.isInTable()) {
            for (TableEntry table : tables) {
                if (paragraph.getStartOffset() >= table.getStartOffset() &&
                    paragraph.getEndOffset() <= table.getEndOffset()) {
                    return true;
                }
            }
        }
        return false;
    }

    private int getTitleLevel(int level) {
        boolean isAvailableLevel = level >= 0 && level < 6;
        return isAvailableLevel ? level + 1 : 0;
    }
}
