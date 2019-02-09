package com.github.verils.transdoc.core.parser.docx;

import com.github.verils.transdoc.core.parser.AbstractPicturePart;

class DocxPicturePart extends AbstractPicturePart {

    private long checksum;

    public long getChecksum() {
        return checksum;
    }

    public void setChecksum(long checksum) {
        this.checksum = checksum;
    }
}
