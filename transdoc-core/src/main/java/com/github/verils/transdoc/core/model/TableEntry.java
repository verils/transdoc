package com.github.verils.transdoc.core.model;

public interface TableEntry extends Entry {

    int getStartOffset();

    int getEndOffset();

    boolean isSingleCellTable();

    TableCellEntry getCell(int row, int col);
}
