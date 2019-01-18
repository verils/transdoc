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
        if (part instanceof TextParagraphPart) {
            return convert((TextParagraphPart) part);
        } else if (part instanceof TitleParagraphPart) {
            return convert((TitleParagraphPart) part);
        } else {
            return EMPTY_STRING;
        }
    }

    private String convert(TextParagraphPart textParagraph) {
        StringBuilder builder = new StringBuilder();
        for (TextPiece textPiece : textParagraph.getTextPieces()) {
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

    private String convert(TitleParagraphPart titleParagraph) {
        String text = titleParagraph.getText();
        int level = titleParagraph.getLevel();
        if (level > 0) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < level; i++) {
                builder.append("#");
            }
            return builder.append(" ").append(text).toString();
        }
        return text;
    }

    private String joinLine(String origin, String append) {
        return join(origin, append, "\n");
    }

    private String joinText(String origin, String append) {
        return join(origin, append, EMPTY_STRING);
    }

    private String join(String origin, String append, String sep) {
        return origin + sep + append;
    }
}
