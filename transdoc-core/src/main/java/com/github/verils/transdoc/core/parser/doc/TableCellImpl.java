package com.github.verils.transdoc.core.parser.doc;

import com.github.verils.transdoc.core.model.Part;
import com.github.verils.transdoc.core.model.TableCell;

import java.util.List;

public class TableCellImpl implements TableCell {

    private final List<Part> entries;

    public TableCellImpl(List<Part> entries) {
        this.entries = entries;
    }
}
