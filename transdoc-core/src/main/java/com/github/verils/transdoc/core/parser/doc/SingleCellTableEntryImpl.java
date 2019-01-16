package com.github.verils.transdoc.core.parser.doc;

import com.github.verils.transdoc.core.model.Part;
import com.github.verils.transdoc.core.model.SingleCellTableEntry;

import java.util.List;

public class SingleCellTableEntryImpl implements SingleCellTableEntry {

    private final String contnet;

    public SingleCellTableEntryImpl(List<Part> contnet) {
        this.contnet = contnet;
    }
}
