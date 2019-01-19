package com.github.verils.transdoc.core.parser.doc;

import com.github.verils.transdoc.core.model.Part;
import com.github.verils.transdoc.core.model.TableCell;

import java.util.List;

public class TableCellImpl implements TableCell {

    private final List<Part> parts;

    public TableCellImpl(List<Part> parts) {
        this.parts = parts;
    }

    @Override
    public List<Part> getParts() {
        return parts;
    }
}
