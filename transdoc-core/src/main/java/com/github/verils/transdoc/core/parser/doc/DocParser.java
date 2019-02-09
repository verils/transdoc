package com.github.verils.transdoc.core.parser.doc;

import com.github.verils.transdoc.core.model.*;
import com.github.verils.transdoc.core.parser.WordParser;
import com.github.verils.transdoc.core.util.StringUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.PicturesTable;
import org.apache.poi.hwpf.usermodel.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DocParser extends WordParser {

    private final HWPFDocument hwpfDocument;

    public DocParser(InputStream input) throws IOException {
        this.hwpfDocument = new HWPFDocument(input);
    }

    @Override
    protected List<PicturePart> parsePictures() {
        PicturesTable picturesTable = hwpfDocument.getPicturesTable();
        List<PicturePart> pictureParts = new ArrayList<PicturePart>();
        List<Picture> allPictures = picturesTable.getAllPictures();
        for (Picture picture : allPictures) {
            pictureParts.add(createPicturePart(picture));
        }
        return pictureParts;
    }

    @Override
    protected List<TablePart> parseTables() {
        TableIterator tableIterator = new TableIterator(hwpfDocument.getRange());
        List<TablePart> tables = new ArrayList<TablePart>();
        while (tableIterator.hasNext()) {
            Table table = tableIterator.next();
            tables.add(createTablePart(table));
        }
        return tables;
    }

    @Override
    protected List<Part> parseParagraphs(List<PicturePart> pictures, List<TablePart> tables) {
        PicturesTable picturesTable = hwpfDocument.getPicturesTable();
        Range documentRange = hwpfDocument.getRange();
        List<Part> parts = new ArrayList<Part>();
        int pictureIndex = 0, tableIndex = 0;
        boolean lastParagraphInTable = false;
        for (int i = 0; i < documentRange.numParagraphs(); i++) {
            Paragraph paragraph = documentRange.getParagraph(i);
            if (hasPicture(picturesTable, paragraph)) {
                PicturePart picturePart = pictures.get(pictureIndex++);
                parts.add(picturePart);
                lastParagraphInTable = false;
            } else if (inTable(tables, paragraph)) {
                if (!lastParagraphInTable) {
                    TablePart tablePart = tables.get(tableIndex++);
                    parts.add(tablePart);
                }
                lastParagraphInTable = true;
            } else {
                ParagraphPart paragraphPart = createParagraphPart(paragraph);
                if (paragraphPart != null) {
                    parts.add(paragraphPart);
                }
                lastParagraphInTable = false;
            }
        }
        return parts;
    }

    private PicturePart createPicturePart(Picture picture) {
        DocPicturePart docPicturePart = new DocPicturePart();
        docPicturePart.setName(picture.suggestFullFileName());
        docPicturePart.setExtension(picture.suggestFileExtension());
        docPicturePart.setData(picture.getContent());
        docPicturePart.setDataOffset(picture.getStartOffset());
        return docPicturePart;
    }

    private TablePart createTablePart(Table table) {
        int rows = table.numRows();
        int cols = table.getRow(0).numCells();
        DocTablePart tablePart = new DocTablePart(rows, cols, table.getStartOffset(), table.getEndOffset());
        for (int i = 0; i < rows; i++) {
            TableRow row = table.getRow(i);
            for (int j = 0; j < row.numCells(); j++) {
                TableCell cell = row.getCell(j);
                TableCellPart tableCellPart = createTableCellPart(cell);
                tablePart.setCell(i, j, tableCellPart);
            }
        }
        return tablePart;
    }

    private TableCellPart createTableCellPart(TableCell cell) {
        List<Part> parts = new ArrayList<Part>();
        for (int i = 0; i < cell.numParagraphs(); i++) {
            Paragraph paragraph = cell.getParagraph(i);
            ParagraphPart paragraphPart = createParagraphPart(paragraph);
            parts.add(paragraphPart);
        }
        return new DocTableCellPart(parts);
    }

    private ParagraphPart createParagraphPart(Paragraph paragraph) {
        String text = paragraph.text();
        if (StringUtils.hasText(text)) {
            int titleLevel = getTitleLevel(paragraph);
            if (isTitle(titleLevel)) {
                text = escapeText(text);
                return new DocParagraphPart(text, titleLevel);
            } else {
                List<TextPiece> textPieces = getTextPieces(paragraph);
                return new DocParagraphPart(textPieces);
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
                DocTablePart tableImpl = (DocTablePart) table;
                if (paragraph.getStartOffset() >= tableImpl.getStartOffset() &&
                    paragraph.getEndOffset() <= tableImpl.getEndOffset()) {
                    return true;
                }
            }
        }
        return false;
    }

    private int getTitleLevel(Paragraph paragraph) {
        int level = paragraph.getLvl();
        boolean isAvailableLevel = level >= 0 && level < 6;
        return isAvailableLevel ? level + 1 : 0;
    }

    private boolean isTitle(int titleLevel) {
        return titleLevel > 0;
    }

    private String escapeText(String text) {
        return Range.stripFields(text)
            .replaceAll("\1|\7|\11|\19|\20|\21|\r|HYPERLINK \".+\"|FORMTEXT", "");
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

            TextPiece textPiece = new DocTextPiece(text, style);
            textPieces.add(textPiece);
        }
        return textPieces;
    }

    @Override
    public void close() throws IOException {
        hwpfDocument.close();
    }
}
