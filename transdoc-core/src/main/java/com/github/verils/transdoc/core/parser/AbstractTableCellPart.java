package com.github.verils.transdoc.core.parser;

import com.github.verils.transdoc.core.model.Part;
import com.github.verils.transdoc.core.model.TableCellPart;

import java.util.List;

public class AbstractTableCellPart implements TableCellPart {

    protected final List<Part> parts;

    public AbstractTableCellPart(List<Part> parts) {
        this.parts = parts;
    }

    @Override
    public List<Part> getParts() {
        return parts;
    }
}
