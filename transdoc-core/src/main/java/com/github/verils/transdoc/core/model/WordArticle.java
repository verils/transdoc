package com.github.verils.transdoc.core.model;

import java.util.ArrayList;
import java.util.List;

public final class WordArticle {

	final List<WordElement> elements;

	final List<WordParagraph> paragraphs;
	final List<WordTable> tables;
	final List<WordPicture> pictures;

	public WordArticle() {
		this.elements = new ArrayList<WordElement>();
		this.paragraphs = new ArrayList<WordParagraph>();
		this.tables = new ArrayList<WordTable>();
		this.pictures = new ArrayList<WordPicture>();
	}

	public List<WordElement> getElements() {
		return elements;
	}

	public List<WordParagraph> getParagraphs() {
		return paragraphs;
	}

	public List<WordTable> getTables() {
		return tables;
	}

	public List<WordPicture> getPictures() {
		return pictures;
	}

	public boolean hasPictures() {
		return !pictures.isEmpty();
	}

}