package com.github.verils.transdoc.core;

import com.github.verils.transdoc.core.model.*;

import java.util.stream.IntStream;

public class MarkdownConverter implements Converter {

    private static final String EMPTY_STRING = "";

    @Override
    public String convert(Article article) {
        return article.getParts()
                .stream()
                .map(this::convertPart)
                .reduce(this::joinLine)
                .orElse(EMPTY_STRING);
    }

    private String convertPart(Part part) {
        if (part instanceof TextParagraph) {
            return convert((TextParagraph) part);
        } else if (part instanceof TitleParagraph) {
            return convert((TitleParagraph) part);
        } else {
            return EMPTY_STRING;
        }
    }

    private String convert(TextParagraph textParagraph) {
        return textParagraph.getTextPieces()
                .stream()
                .map(this::convert)
                .reduce(this::joinText)
                .orElse("");
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

    private String convert(TitleParagraph titleParagraph) {
        String text = titleParagraph.getText();
        int level = titleParagraph.getLevel();
        if (level > 0) {
            StringBuilder builder = new StringBuilder();
            IntStream.range(0, level).forEach(i -> builder.append("#"));
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
