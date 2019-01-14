package com.github.verils.transdoc.core.parser;

import com.github.verils.transdoc.core.model.ParagraphPart;
import com.github.verils.transdoc.core.model.Part;
import com.github.verils.transdoc.core.model.TitlePart;
import com.github.verils.transdoc.core.old.model.Article;
import com.github.verils.transdoc.core.util.StringUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

class DocParser extends WordParser {

    private final HWPFDocument document;

    private final Range documentRange;

    DocParser(InputStream source) throws IOException {
        this.document = new HWPFDocument(source);
        this.documentRange = this.document.getRange();
    }

    @Override
    public Article getArticle() {
        parse();
        return new Article();
    }

    private void parse() {
        List<Part> parts = new ArrayList<>();

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
                TitlePart titlePart = new TitlePart();
            } else {
                ParagraphPart paragraphPart = new ParagraphPart();
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
}
