package com.github.verils.transdoc.core.model;

import java.util.Arrays;

public class Table extends Part {
	private int rownum;
	private int colnum;
	private boolean isBlock;
	private Article[][] data;

	public Table(int rownum, int colnum) {
		super(PartType.TABLE);
		this.rownum = rownum;
		this.colnum = colnum;
		this.isBlock = (rownum == 1 && colnum == 1);
		this.data = new Article[rownum][colnum];
	}

	public Article[] getRow(int rownum) {
		return this.data[rownum];
	}

	public void setRow(int rownum, Article[] row) {
		if ((rownum >= 0) && (rownum < this.rownum))
			this.data[rownum] = row;
	}

	public Article getCell(int rownum, int colnum) {
		return this.data[rownum][colnum];
	}

	public void setCell(int rownum, int colnum, Article content) {
		if ((rownum >= 0) && (rownum < this.rownum) && (colnum >= 0) && (colnum < this.colnum))
			this.data[rownum][colnum] = content;
	}

	public int getRownum() {
		return this.rownum;
	}

	public int getColnum() {
		return this.colnum;
	}

	public boolean isBlock() {
		return this.isBlock;
	}

	public String toString() {
		return "DocTable [rownum=" + this.rownum + ", colnum=" + this.colnum + ", codeBlock="
				+ this.isBlock + ", data=" + Arrays.toString(this.data) + "]";
	}
}