package com.github.verils.transdoc.core.parser.doc;

import com.github.verils.transdoc.core.model.Part;
import com.github.verils.transdoc.core.model.TableCellPart;

import java.util.List;

public class TableCellPartImpl implements TableCellPart {

    private final List<Part> entries;

    public TableCellPartImpl(List<Part> entries) {
        this.entries = entries;
    }
}
