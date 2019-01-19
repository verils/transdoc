package com.github.verils.transdoc.core.model;

public interface TablePart extends Part {

    int getRows();

    int getCols();

    boolean isSingleCellTable();

    TableCell getCell(int row, int col);
}
