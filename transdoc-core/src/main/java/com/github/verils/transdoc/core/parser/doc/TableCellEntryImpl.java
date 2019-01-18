package com.github.verils.transdoc.core.parser.doc;

import com.github.verils.transdoc.core.model.Entry;
import com.github.verils.transdoc.core.model.TableCellEntry;

import java.util.List;

public class TableCellEntryImpl implements TableCellEntry {

    private final List<Entry> entries;

    public TableCellEntryImpl(List<Entry> entries) {
        this.entries = entries;
    }
}
