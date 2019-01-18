package com.github.verils.transdoc.core.model;

public interface TablePart extends Part {

    int getStartOffset();

    int getEndOffset();

    boolean isSingleCellTable();

    TableCell getCell(int row, int col);
}
