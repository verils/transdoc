package com.github.verils.transdoc.core;

import com.github.verils.transdoc.core.model.*;

public class MarkdownConverter implements Converter {

    private static final String EMPTY_STRING = "";

    @Override
    public String convert(WordDocument article) {
        StringBuilder builder = new StringBuilder();
//        for (Part part : article.getParts()) {
//            String mdParagraph = convertPart(part);
//            builder.append(mdParagraph);
//        }
//        return builder.toString();
        return null;
    }

    private String convertPart(Part part) {
        if (part instanceof ParagraphPart) {
            return convert((ParagraphPart) part);
        } else {
            return EMPTY_STRING;
        }
    }

    private String convert(ParagraphPart paragraph) {
        if (paragraph.isTitle()) {
            return convertTitle(paragraph);
        } else {
            return convertText(paragraph);
        }
    }

    private String convertTitle(ParagraphPart paragraph) {
        StringBuilder builder = new StringBuilder();
        for (TextPiece textPiece : paragraph.getTextPieces()) {
            String text = convert(textPiece);
            builder.append(text);
        }
        return builder.toString();
    }

    private String convert(TextPiece textPiece) {
        String text = textPiece.getText();
        if (textPiece.isBold()) {
            return "**" + text + "**";
        } else if (textPiece.isItalic()) {
            return "*" + text + "*";
        } else {
            return text;
        }
    }

    private String convertText(ParagraphPart paragraph) {
        String text = paragraph.getText();
        int titleLevel = paragraph.getTitleLevel();
        if (titleLevel > 0) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < titleLevel; i++) {
                builder.append("#");
            }
            return builder.append(" ").append(text).toString();
        }
        return text;
    }
}
