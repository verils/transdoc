package com.transdoc.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.PicturesTable;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;

import com.transdoc.core.model.WordArticle;
import com.transdoc.core.model.WordElement;
import com.transdoc.core.model.WordParagraph;
import com.transdoc.core.model.WordPicture;
import com.transdoc.core.model.WordTable;

public class DocParser extends WordParser {
	private HWPFDocument doc;
	private Range docRange;

	DocParser(InputStream input) throws IOException {
		this.doc = new HWPFDocument(input);
		this.docRange = this.doc.getRange();
	}

	public WordArticle parse() {
		WordArticle article = new WordArticle();
		parseDocument(article);
		return article;
	}

	private void parseDocument(WordArticle article) {
		List<WordElement> elements = article.getElements();
		List<WordParagraph> paragraphs = article.getParagraphs();
		List<WordTable> tables = article.getTables();
		List<WordPicture> pictures = article.getPictures();

		parsePictures(pictures);
		parseTables(tables);

		int pictureIndex = 0, tableIndex = 0;
		int numParagraphs = docRange.numParagraphs();
		for (int i = 0; i < numParagraphs; ++i) {
			Paragraph paragraph = docRange.getParagraph(i);
			if (hasPicture(paragraph, pictures, pictureIndex)) {
				elements.add(pictures.get(pictureIndex));
				pictureIndex++;
			} else if (paragraph.isInTable()) {
				if (!elements.isEmpty()) {
					// 判断是否为空,防止数组越界
					WordElement lastWordElement = elements.get(elements.size() - 1);
					if (lastWordElement.getType() != WordElement.Type.TABLE) {
						elements.add(tables.get(tableIndex));
						tableIndex++;
					}
				}
			} else {
				// 普通段落
				String text = paragraph.text();
				if (text != null && !"".equals(text.trim())) {
					int titleLevel = getTitleLevel(paragraph.getLvl());
					String paragraphContent = "";
					if (titleLevel > 0) {
						paragraphContent = getTitleContent(paragraph);
					} else {
						paragraphContent = getStyledContent(paragraph);
					}
					WordParagraph wordParagraph = new WordParagraph(paragraphContent);
					wordParagraph.setTitleLvl(titleLevel);
					wordParagraph.setInList(paragraph.isInList());
					paragraphs.add(wordParagraph);
					elements.add(wordParagraph);
				}
			}
		}
	}

	private void parsePictures(List<WordPicture> pictures) {
		PicturesTable picturesTable = doc.getPicturesTable();
		if (savePictures) {
			int numCharacterRuns = docRange.numCharacterRuns();
			for (int i = 0; i < numCharacterRuns; i++) {
				CharacterRun characterRun = docRange.getCharacterRun(i);
				if (picturesTable.hasPicture(characterRun)) {
					Picture picture = picturesTable.extractPicture(characterRun, false);

					WordPicture wordPicture = new DocPictureWrapper(characterRun, picture);
					wordPicture.setIndex(pictures.size() + 1);
					wordPicture.setPictureName(picture.suggestFullFileName());
					wordPicture.setExtension(picture.suggestFileExtension());
					wordPicture.setRelativePath(getImagePath(wordPicture));
					pictures.add(wordPicture);
				}
			}
		}
	}

	private void parseTables(List<WordTable> tables) {
		TableIterator tableIterator = new TableIterator(docRange);
		while (tableIterator.hasNext()) {
			Table table = tableIterator.next();
			int numRows = table.numRows();
			TableRow tr = table.getRow(0);
			int numCells = tr.numCells();
			WordTable docTable = null;
			if ((numRows == 1) && (numCells == 1)) {
				docTable = new WordTable(numRows, numCells);
				WordArticle cellContent = getCellContent(tr.getCell(0), true);
				docTable.setCell(0, 0, cellContent);
			} else {
				int maxNumCells = numCells;
				for (int i = 1; i < numRows; ++i) {
					tr = table.getRow(i);
					numCells = tr.numCells();
					maxNumCells = (maxNumCells >= numCells) ? maxNumCells : numCells;
				}
				docTable = new WordTable(numRows, maxNumCells);
				for (int i = 0; i < numRows; ++i) {
					tr = table.getRow(i);
					numCells = tr.numCells();
					for (int j = 0; j < maxNumCells; ++j) {
						WordArticle cellContent = new WordArticle();
						if (j < numCells) {
							cellContent = getCellContent(tr.getCell(j), false);
						}
						docTable.setCell(i, j, cellContent);
					}
				}
			}
			tables.add(docTable);
		}
	}

	private boolean hasPicture(Paragraph paragraph, List<WordPicture> pictures, int pictureIndex) {
		if (!savePictures) {
			return false;
		}
		if (pictureIndex >= pictures.size()) {
			return false;
		}
		DocPictureWrapper docPicture = (DocPictureWrapper) pictures.get(pictureIndex);
		CharacterRun characterRun = docPicture.characterRun;
		return characterRun.getStartOffset() >= paragraph.getStartOffset()
				&& characterRun.getEndOffset() <= paragraph.getEndOffset();
	}

	private int getTitleLevel(int lvl) {
		if (lvl >= 0 && lvl < 6) {
			return ++lvl;
		}
		return 0;
	}

	private String getTitleContent(Paragraph paragraph) {
		return cleanText(paragraph.text());
	}

	private String getStyledContent(Paragraph paragraph) {
		StringBuilder text = new StringBuilder();
		int numCharacterRuns = paragraph.numCharacterRuns();
		for (int j = 0; j < numCharacterRuns; ++j) {
			CharacterRun characterRun = paragraph.getCharacterRun(j);
			String prefix = "";
			String suffix = "";
			String characterRunText = characterRun.text();
			if (!"".equals(characterRunText.trim())) {
				if (characterRun.isBold()) {
					prefix = suffix = "**";
					characterRunText = StringUtils.rtrim(characterRunText);
				} else if (characterRun.isItalic()) {
					prefix = suffix = "*";
					characterRunText = StringUtils.rtrim(characterRunText);
				}
			}
			if (text.length() > 0 && text.charAt(text.length() - 1) == '*') {
				text.append(" ");
			}
			text.append(prefix).append(characterRunText).append(suffix);
		}
		return cleanText(text.toString()).trim();
	}

	private WordArticle getCellContent(TableCell cell, boolean isBlock) {
		WordArticle cellArticle = new WordArticle();
		List<WordElement> cellElements = cellArticle.getElements();
		List<WordParagraph> cellParagraphs = cellArticle.getParagraphs();

		int numParagraphs = cell.numParagraphs();
		for (int k = 0; k < numParagraphs; ++k) {
			Paragraph paragraph = cell.getParagraph(k);
			String text = paragraph.text();
			text = cleanText(text);
			if (!("".equals(text.trim()))) {
				text = isBlock ? StringUtils.rtrim(text) : text.trim();
				if (isBlock && paragraph.getFirstLineIndent() >= 400) {
					text = "\t" + text;
				}

				WordParagraph wordParagraph = new WordParagraph(text);
				cellElements.add(wordParagraph);
				cellParagraphs.add(wordParagraph);
			}
		}
		return cellArticle;
	}

	private String cleanText(String text) {
		return Range.stripFields(text).replaceAll("\1|\11|\19|\20|\21|HYPERLINK \".+\"|FORMTEXT", "");
	}
}

class DocPictureWrapper extends WordPicture {
	CharacterRun characterRun;
	Picture picture;

	DocPictureWrapper(CharacterRun characterRun, Picture picture) {
		this.characterRun = characterRun;
		this.picture = picture;
	}

	public byte[] getData() {
		return this.picture.getContent();
	}

	public void writeTo(OutputStream out) throws IOException {
		this.picture.writeImageContent(out);
	}
}