package com.github.verils.transdoc.core.model;

import java.util.ArrayList;
import java.util.List;

public final class Article {

	private final List<Part> parts;

	private final List<Paragraph> paragraphs;
	private final List<Table> tables;
	private final List<Picture> pictures;

	public Article() {
		this.parts = new ArrayList<Part>();
		this.paragraphs = new ArrayList<Paragraph>();
		this.tables = new ArrayList<Table>();
		this.pictures = new ArrayList<Picture>();
	}

	public List<Part> getParts() {
		return parts;
	}

	public List<Paragraph> getParagraphs() {
		return paragraphs;
	}

	public List<Table> getTables() {
		return tables;
	}

	public List<Picture> getPictures() {
		return pictures;
	}

	public boolean hasPictures() {
		return !pictures.isEmpty();
	}

}