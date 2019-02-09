package com.github.verils.transdoc.core.parser.docx;

import com.github.verils.transdoc.core.model.*;
import com.github.verils.transdoc.core.model.TextPiece.Style;
import com.github.verils.transdoc.core.parser.WordParser;
import com.github.verils.transdoc.core.util.StringUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DocxParser extends WordParser {

    private final XWPFDocument xwpfDocument;

    public DocxParser(InputStream input) throws IOException {
        this.xwpfDocument = new XWPFDocument(input);
    }

    @Override
    protected List<PicturePart> parsePictures() {
        List<XWPFPictureData> allPictures = xwpfDocument.getAllPictures();
        List<PicturePart> pictureParts = new ArrayList<PicturePart>();
        for (XWPFPictureData picture : allPictures) {
            pictureParts.add(createPicturePart(picture));
        }
        return pictureParts;
    }

    @Override
    protected List<TablePart> parseTables() {
        Iterator<XWPFTable> tablesIterator = xwpfDocument.getTablesIterator();
        List<TablePart> tables = new ArrayList<TablePart>();
        while (tablesIterator.hasNext()) {
            XWPFTable table = tablesIterator.next();
            tables.add(createTablePart(table));
        }
        return tables;
    }

    @Override
    protected List<Part> parseParagraphs(List<PicturePart> pictures, List<TablePart> tables) {
        // TODO Finish this method.
        return null;
    }

    private PicturePart createPicturePart(XWPFPictureData picture) {
        DocxPicturePart docxPicturePart = new DocxPicturePart();
        docxPicturePart.setName(picture.getFileName());
        docxPicturePart.setExtension(picture.suggestFileExtension());
        docxPicturePart.setData(picture.getData());
        docxPicturePart.setChecksum(picture.getChecksum());
        return docxPicturePart;
    }

    private TablePart createTablePart(XWPFTable table) {
        int rows = table.getNumberOfRows();
        int cols = table.getRow(0).getTableCells().size();
        DocxTablePart tablePart = new DocxTablePart(rows, cols);
        for (int i = 0; i < rows; i++) {
            XWPFTableRow row = table.getRow(i);
            for (int j = 0; j < cols; j++) {
                XWPFTableCell cell = row.getCell(j);
                TableCellPart tableCellPart = createTableCellPart(cell);
                tablePart.setCell(i, j, tableCellPart);
            }
        }
        return tablePart;
    }

    private TableCellPart createTableCellPart(XWPFTableCell cell) {
        List<XWPFParagraph> paragraphs = cell.getParagraphs();
        List<Part> parts = new ArrayList<Part>();
        for (XWPFParagraph paragraph : paragraphs) {
            ParagraphPart paragraphPart = createParagraphPart(paragraph);
            parts.add(paragraphPart);
        }
        return new DocxTableCellPart(parts);
    }

    private ParagraphPart createParagraphPart(XWPFParagraph paragraph) {
        String text = paragraph.getParagraphText();
        if (StringUtils.hasText(text)) {
            int titleLevel = getTitleLevel(paragraph);
            if (isTitle(titleLevel)) {
                text = escapeText(text);
                return new DocxParagraphPart(text, titleLevel);
            } else {
                List<TextPiece> textPieces = getTextPieces(paragraph);
                return new DocxParagraphPart(textPieces);
            }
        }
        return null;
    }

    private int getTitleLevel(XWPFParagraph paragraph) {
        CTP ctp = paragraph.getCTP();
        CTPPr pPr = ctp.getPPr();
        if (pPr == null) {
            return 0;
        }
        CTDecimalNumber outlineLvl = pPr.getOutlineLvl();
        if (outlineLvl == null) {
            return 0;
        }
        int level = outlineLvl.getVal().intValue() + 1;
        boolean isAvailableLevel = level >= 0 && level < 6;
        return isAvailableLevel ? level + 1 : 0;
    }

    private boolean isTitle(int titleLevel) {
        return titleLevel > 0;
    }

    private String escapeText(String text) {
        return text;
    }

    private List<TextPiece> getTextPieces(XWPFParagraph paragraph) {
        List<XWPFRun> runs = paragraph.getRuns();
        List<TextPiece> textPieces = new ArrayList<TextPiece>(runs.size());
        for (XWPFRun run : runs) {
            String text = run.text();
            text = escapeText(text);
            Style style = Style.NONE;
            if (run.isBold()) {
                style = Style.BOLD;
            } else if (run.isItalic()) {
                style = Style.ITALIC;
            }
            TextPiece textPiece = new DocxTextPiece(text, style);
            textPieces.add(textPiece);
        }
        return textPieces;
    }

    @Override
    public void close() throws IOException {
        xwpfDocument.close();
    }
}
