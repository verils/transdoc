package com.github.verils.transdoc.core.parser.doc;

import com.github.verils.transdoc.core.model.TableCell;
import com.github.verils.transdoc.core.model.TablePart;

public class TablePartImpl implements TablePart {

    private final int startOffset;

    private final int endOffset;

    private final int rows;

    private final int cols;

    private final TableCell[][] matrix;

    public TablePartImpl(int startOffset, int endOffset, int rows, int cols) {
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.rows = rows;
        this.cols = cols;
        this.matrix = new TableCell[rows][cols];
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
    public TableCell getCell(int row, int col) {
        return matrix[row][col];
    }

    public void setCell(int row, int col, TableCell tableCellEntry) {
        matrix[row][col] = tableCellEntry;
    }
}
