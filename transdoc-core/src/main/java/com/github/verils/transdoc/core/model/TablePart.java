package com.github.verils.transdoc.core.model;

public interface TablePart extends Part {

    int getStartOffset();

    int getEndOffset();

    boolean isSingleCellTable();

    TableCellPart getCell(int row, int col);
}
