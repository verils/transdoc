package com.github.verils.transdoc.core.parser;

import com.github.verils.transdoc.core.model.TableCellPart;
import com.github.verils.transdoc.core.model.TablePart;

public abstract class AbstractTablePart implements TablePart {

    private final int rows;
    private final int cols;
    private final TableCellPart[][] table;

    public AbstractTablePart(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.table = new TableCellPart[rows][cols];
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public int getCols() {
        return cols;
    }

    @Override
    public boolean isSingleCellTable() {
        return rows == 1 && cols == 1;
    }

    @Override
    public TableCellPart getCell(int row, int col) {
        return table[row][col];
    }

    public void setCell(int row, int col, TableCellPart tableCellPartEntry) {
        table[row][col] = tableCellPartEntry;
    }

    @Override
    public String toString() {
        return "AbstractTablePart{" +
            "rows=" + getRows() +
            ", cols=" + getCols() +
            '}';
    }
}
