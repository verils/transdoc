package com.github.verils.transdoc.core.parser.docx;

import com.github.verils.transdoc.core.model.TextPiece;
import com.github.verils.transdoc.core.parser.AbstractParagraphPart;

import java.util.List;

class DocxParagraphPart extends AbstractParagraphPart {

    DocxParagraphPart(String text, int titleLevel) {
        super(text, titleLevel);
    }

    DocxParagraphPart(List<TextPiece> textPieces) {
        super(textPieces);
    }
}
