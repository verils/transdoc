package com.github.verils.transdoc.core.model;

import java.io.IOException;
import java.io.OutputStream;

public abstract class Picture extends Part {

	private int index;
	private String pictureName;
	private String extension;
	private String relativePath;

	public Picture() {
		super(PartType.PICTURE);
	}

	public String getPictureName() {
		return this.pictureName;
	}

	public void setPictureName(String pictureName) {
		this.pictureName = pictureName;
	}

	public String getExtension() {
		return this.extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getRelativePath() {
		return this.relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public abstract byte[] getData();

	public abstract void writeTo(OutputStream paramOutputStream) throws IOException;

	public int getIndex() {
		return this.index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}