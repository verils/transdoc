package com.github.verils.transdoc.core.parser.doc;

import com.github.verils.transdoc.core.parser.AbstractPicturePart;

class DocPicturePart extends AbstractPicturePart {

    private int dataOffset;

    public int getDataOffset() {
        return dataOffset;
    }

    void setDataOffset(int dataOffset) {
        this.dataOffset = dataOffset;
    }
}
