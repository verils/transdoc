package com.github.verils.transdoc.core.converter;

import com.github.verils.transdoc.core.model.*;
import com.github.verils.transdoc.core.util.StringUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MarkdownConverter implements Converter {

    private String pictureDir = "pictures";

    @Override
    public String convert(WordDocument wordDocument) {
        StringBuilder builder = new StringBuilder();
        appendParts(builder, wordDocument.getParts());
        return builder.toString();
    }

    @Override
    public void extractPictures(WordDocument wordDocument, File dest) throws IOException {
        for (PicturePart picture : wordDocument.getPictures()) {
            File picFile = new File(dest, getPath(picture));
            picFile.mkdirs();
            FileUtils.writeByteArrayToFile(picFile, picture.getData());
        }
    }

    @Override
    public String getPictureDir() {
        return pictureDir.endsWith("/") ? pictureDir : pictureDir + "/";
    }

    @Override
    public void setPictureDir(String pictureDir) {
        this.pictureDir = pictureDir;
    }

    private void appendParts(StringBuilder builder, List<Part> parts) {
        for (Part part : parts) {
            if (part instanceof ParagraphPart) {
                appendParagraph(builder, (ParagraphPart) part);
            } else if (part instanceof TablePart) {
                appendTable(builder, (TablePart) part);
            } else if (part instanceof PicturePart) {
                appendPicture(builder, (PicturePart) part);
            }
        }
    }

    private void appendParagraph(StringBuilder builder, ParagraphPart paragraph) {
        if (paragraph.isTitle()) {
            appendTitle(builder, paragraph);
        } else {
            appendText(builder, paragraph);
        }
        builder.append("\n\n");
    }

    private void appendTable(StringBuilder builder, TablePart table) {
        if (table.isSingleCellTable()) {
            appendCodeBlock(builder, table);
        } else {
            appendTableGrid(builder, table);
        }
        builder.append("\n\n");
    }

    private void appendPicture(StringBuilder builder, PicturePart picture) {
        builder.append("[](").append(getPath(picture)).append(")");
        builder.append("\n\n");
    }

    private void appendTitle(StringBuilder builder, ParagraphPart paragraph) {
        if (paragraph.getTitleLevel() > 0) {
            for (int i = 0; i < paragraph.getTitleLevel(); i++) {
                builder.append("#");
            }
            builder.append(" ").append(paragraph.getText());
        }
    }

    private void appendText(StringBuilder builder, ParagraphPart paragraph) {
        for (TextPiece textPiece : paragraph.getTextPieces()) {
            String text = textPiece.getText();
            if (textPiece.isBold()) {
                appendBoldText(builder, text);
            } else if (textPiece.isItalic()) {
                appendItalicText(builder, text);
            } else {
                appendText(builder, text);
            }
        }
    }

    private void appendCodeBlock(StringBuilder builder, TablePart table) {
        TableCellPart cell = table.getCell(0, 0);
        builder.append("```\n");
        for (Part part : cell.getParts()) {
            if (part instanceof ParagraphPart) {
                ParagraphPart paragraph = (ParagraphPart) part;
                builder.append(paragraph.getText()).append("\n");
            }
        }
        builder.append("```");
    }

    private void appendTableGrid(StringBuilder builder, TablePart table) {
        int rows = table.getRows();
        int cols = table.getCols();
        for (int i = 0; i < rows; i++) {
            builder.append("|");
            for (int j = 0; j < cols; j++) {
                TableCellPart cell = table.getCell(i, j);
                StringBuilder cellBuilder = new StringBuilder();
                appendParts(cellBuilder, cell.getParts());
                String cellContent = cellBuilder.toString().replaceAll("\n", "<br>");
                builder.append(cellContent).append("|");
            }
            builder.append("\n");

            // Second table line
            if (i == 0) {
                builder.append("|");
                for (int j = 0; j < cols; j++) {
                    builder.append("----|");
                }
                builder.append("\n");
            }
        }
    }

    private void appendBoldText(StringBuilder builder, String text) {
        appendSpaceWhenTailing(builder);
        builder.append("**").append(text).append("**");
    }

    private void appendItalicText(StringBuilder builder, String text) {
        appendSpaceWhenTailing(builder);
        builder.append("*").append(text).append("*");
    }

    private void appendText(StringBuilder builder, String text) {
        appendSpaceWhenTailing(builder);
        builder.append(text);
    }

    private void appendSpaceWhenTailing(StringBuilder builder) {
        if (builder.length() > 0 && builder.charAt(builder.length() - 1) == '*') {
            builder.append(" ");
        }
    }

    private String getPath(PicturePart picture) {
        return getPictureDir() + StringUtils.numberToString(picture.getIndex(), 4) + "." + picture.getExtension();
    }
}
