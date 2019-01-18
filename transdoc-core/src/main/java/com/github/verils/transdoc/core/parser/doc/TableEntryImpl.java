package com.github.verils.transdoc.core.parser.doc;

import com.github.verils.transdoc.core.model.TableCellEntry;
import com.github.verils.transdoc.core.model.TableEntry;

public class TableEntryImpl implements TableEntry {

    private final int startOffset;

    private final int endOffset;

    private final int rows;

    private final int cols;

    private final TableCellEntry[][] matrix;

    public TableEntryImpl(int startOffset, int endOffset, int rows, int cols) {
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.rows = rows;
        this.cols = cols;
        this.matrix = new TableCellEntry[rows][cols];
    }

    @Override
    public int getStartOffset() {
        return startOffset;
    }

    @Override
    public int getEndOffset() {
        return endOffset;
    }

    @Override
    public boolean isSingleCellTable() {
        return rows == 1 && cols == 1;
    }

    @Override
    public TableCellEntry getCell(int row, int col) {
        return matrix[row][col];
    }

    public void setCell(int row, int col, TableCellEntry tableCellEntry) {
        matrix[row][col] = tableCellEntry;
    }
}
