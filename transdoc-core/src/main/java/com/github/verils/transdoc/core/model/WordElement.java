package com.github.verils.transdoc.core.model;

public abstract class WordElement {
	public static enum Type {
		PARAGRAPH, TABLE, PICTURE
	}

	final Type type;
	boolean inList;

	WordElement(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	public boolean isInList() {
		return inList;
	}

	public void setInList(boolean inList) {
		this.inList = inList;
	}

}