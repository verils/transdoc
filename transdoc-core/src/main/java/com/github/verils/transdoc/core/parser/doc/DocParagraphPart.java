package com.github.verils.transdoc.core.parser.doc;

import com.github.verils.transdoc.core.model.TextPiece;
import com.github.verils.transdoc.core.parser.AbstractParagraphPart;

import java.util.List;

class DocParagraphPart extends AbstractParagraphPart {

    DocParagraphPart(String text, int titleLevel) {
        super(text, titleLevel);
    }

    DocParagraphPart(List<TextPiece> textPieces) {
        super(textPieces);
    }
}
