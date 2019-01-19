package com.github.verils.transdoc.core.parser;

import com.github.verils.transdoc.core.model.*;
import com.github.verils.transdoc.core.model.TableCell;
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

    @Override
    public WordDocument parse(InputStream source) throws IOException {
        HWPFDocument hwpfDocument = new HWPFDocument(source);

        List<PicturePart> pictureEntries = parsePictures(hwpfDocument);
        List<TablePart> tableEntries = parseTables(hwpfDocument);
        List<Part> entries = parseParagraphs(hwpfDocument, pictureEntries, tableEntries);

        hwpfDocument.close();

        WordDocumentImpl docWordDocument = new WordDocumentImpl();
        docWordDocument.setPictures(pictureEntries);
        docWordDocument.setTables(tableEntries);
        docWordDocument.setParts(entries);
        return docWordDocument;
    }

    /**
     * 解析图片
     *
     * @param hwpfDocument hwpfDocument
     * @return 图片列表
     */
    private List<PicturePart> parsePictures(HWPFDocument hwpfDocument) {
        List<PicturePart> pictureEntries = new ArrayList<PicturePart>();
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
    private List<TablePart> parseTables(HWPFDocument hwpfDocument) {
        List<TablePart> tables = new ArrayList<TablePart>();
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
    private List<Part> parseParagraphs(HWPFDocument hwpfDocument, List<PicturePart> pictures, List<TablePart> tables) {
        List<Part> entries = new ArrayList<Part>();
        PicturesTable picturesTable = hwpfDocument.getPicturesTable();
        Range documentRange = hwpfDocument.getRange();
        int pictureIndex = 0, tableIndex = 0;
        boolean lastParagraphInTable = false;
        for (int i = 0; i < documentRange.numParagraphs(); i++) {
            Paragraph paragraph = documentRange.getParagraph(i);
            if (hasPicture(picturesTable, paragraph)) {
                PicturePart pictureEntry = pictures.get(pictureIndex++);
                entries.add(pictureEntry);
                lastParagraphInTable = false;
            } else if (inTable(tables, paragraph)) {
                if (!lastParagraphInTable) {
                    TablePart tableEntry = tables.get(tableIndex++);
                    entries.add(tableEntry);
                }
                lastParagraphInTable = true;
            } else {
                ParagraphPart paragraphEntry = createParagraphEntry(paragraph);
                if (paragraphEntry != null) {
                    entries.add(paragraphEntry);
                }
                lastParagraphInTable = false;
            }
        }
        return entries;
    }

    private PicturePart createPictureEntry(int index, Picture picture) {
        PicturePartImpl pictureEntry = new PicturePartImpl();
        pictureEntry.setId(index);
        pictureEntry.setName(picture.suggestFullFileName());
        pictureEntry.setExtension(picture.suggestFileExtension());
        pictureEntry.setDataOffset(picture.getStartOffset());
        pictureEntry.setData(picture.getContent());
        return pictureEntry;
    }

    private TablePart createTableEntry(Table table) {
        int rows = table.numRows();
        int cols = table.getRow(0).numCells();
        TablePartImpl tableEntry = new TablePartImpl(table.getStartOffset(), table.getEndOffset(), rows, cols);
        for (int i = 0; i < rows; i++) {
            TableRow row = table.getRow(i);
            for (int j = 0; j < row.numCells(); j++) {
                org.apache.poi.hwpf.usermodel.TableCell cell = row.getCell(j);
                TableCell tableCellEntry = createTableCellEntry(cell);
                tableEntry.setCell(i, j, tableCellEntry);
            }
        }
        return tableEntry;
    }

    private TableCell createTableCellEntry(org.apache.poi.hwpf.usermodel.TableCell cell) {
        List<Part> entries = new ArrayList<Part>();
        for (int i = 0; i < cell.numParagraphs(); i++) {
            Paragraph paragraph = cell.getParagraph(i);
            ParagraphPart entry = createParagraphEntry(paragraph);
            entries.add(entry);
        }
        return new TableCellImpl(entries);
    }

    private ParagraphPart createParagraphEntry(Paragraph paragraph) {
        String text = paragraph.text();
        if (StringUtils.hasText(text)) {
            int titleLevel = getTitleLevel(paragraph.getLvl());
            if (isTitle(titleLevel)) {
                text = escapeText(text);
                return new ParagraphPartImpl(text, titleLevel);
            } else {
                List<TextPiece> textPieces = getTextPieces(paragraph);
                return new ParagraphPartImpl(textPieces);
            }
        }
        return null;
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

    private boolean inTable(List<TablePart> tables, Paragraph paragraph) {
        if (paragraph.isInTable()) {
            for (TablePart table : tables) {
                TablePartImpl tableImpl = (TablePartImpl) table;
                if (paragraph.getStartOffset() >= tableImpl.getStartOffset() &&
                    paragraph.getEndOffset() <= tableImpl.getEndOffset()) {
                    return true;
                }
            }
        }
        return false;
    }

    private String escapeText(String text) {
        return Range.stripFields(text)
            .replaceAll("\1|\7|\11|\19|\20|\21|\r|HYPERLINK \".+\"|FORMTEXT", "");
    }

    private int getTitleLevel(int level) {
        boolean isAvailableLevel = level >= 0 && level < 6;
        return isAvailableLevel ? level + 1 : 0;
    }

    private boolean isTitle(int titleLevel) {
        return titleLevel > 0;
    }

    private List<TextPiece> getTextPieces(Paragraph paragraph) {
        int numCharacterRuns = paragraph.numCharacterRuns();
        List<TextPiece> textPieces = new ArrayList<TextPiece>(numCharacterRuns);
        for (int i = 0; i < numCharacterRuns; i++) {
            CharacterRun characterRun = paragraph.getCharacterRun(i);

            String text = characterRun.text();
            text = escapeText(text);

            TextPiece.Style style = TextPiece.Style.NONE;
            if (characterRun.isBold()) {
                style = TextPiece.Style.BOLD;
            } else if (characterRun.isItalic()) {
                style = TextPiece.Style.ITALIC;
            }

            TextPiece textPiece = new TextPieceImpl(text, style);
            textPieces.add(textPiece);
        }
        return textPieces;
    }
}
