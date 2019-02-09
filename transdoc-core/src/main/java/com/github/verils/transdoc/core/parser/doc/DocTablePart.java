package com.github.verils.transdoc.core.parser.doc;

import com.github.verils.transdoc.core.parser.AbstractTablePart;

class DocTablePart extends AbstractTablePart {

    private final int startOffset;

    private final int endOffset;

    DocTablePart(int rows, int cols, int startOffset, int endOffset) {
        super(rows, cols);
        this.startOffset = startOffset;
        this.endOffset = endOffset;
    }

    int getStartOffset() {
        return startOffset;
    }

    int getEndOffset() {
        return endOffset;
    }
}
