package com.github.verils.transdoc.core;

import com.github.verils.transdoc.core.model.WordArticle;
import com.github.verils.transdoc.core.model.WordElement;
import com.github.verils.transdoc.core.model.WordParagraph;
import com.github.verils.transdoc.core.model.WordPicture;
import com.github.verils.transdoc.core.model.WordTable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;

public class DocxParser extends WordParser {
	private XWPFDocument doc;

	DocxParser(InputStream input) throws IOException {
		this.doc = new XWPFDocument(input);
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

		boolean isListStart = false;
		boolean isInList = false;

		Iterator<IBodyElement> bodyElementsIterator = doc.getBodyElementsIterator();
		while (bodyElementsIterator.hasNext()) {
			IBodyElement bodyElement = (IBodyElement) bodyElementsIterator.next();
			BodyElementType elementType = bodyElement.getElementType();
			switch (elementType) {
			case PARAGRAPH:
				XWPFParagraph paragraph = (XWPFParagraph) bodyElement;
				isListStart = false;
				boolean inList = !isListStart && isInList;

				if (savePictures) {
					List<XWPFRun> runs = paragraph.getRuns();
					for (XWPFRun run : runs) {
						List<XWPFPicture> embeddedPictures = run.getEmbeddedPictures();
						for (XWPFPicture xwpfPicture : embeddedPictures) {
							XWPFPictureData pictureData = xwpfPicture.getPictureData();

							WordPicture wordPicture = new DocxPictureWrapper(pictureData, inList);
							wordPicture.setIndex(pictures.size() + 1);
							wordPicture.setPictureName(pictureData.getFileName());
							wordPicture.setExtension(pictureData.suggestFileExtension());
							wordPicture.setRelativePath(getImagePath(wordPicture));
							pictures.add(wordPicture);
							elements.add(wordPicture);
						}
					}
				}

				String text = paragraph.getParagraphText().trim();
				if (!paragraph.isEmpty() && !"".equals(text)) {
					WordParagraph wordParagraph = new WordParagraph(text);
					wordParagraph.setTitleLvl(getTitleLevel(paragraph));
					wordParagraph.setInList(inList);
					paragraphs.add(wordParagraph);
					elements.add(wordParagraph);
				}
				break;

			case TABLE:
				XWPFTable table = (XWPFTable) bodyElement;
				WordTable docTable = null;

				int numRows = table.getNumberOfRows();
				XWPFTableRow tr = table.getRow(0);
				List<XWPFTableCell> tds = tr.getTableCells();
				int numCells = tds.size();
				if ((numRows == 1) && (numCells == 1)) {
					docTable = new WordTable(numRows, numCells);
					docTable.setInList(isInList);
					WordArticle cellContent = getCellContent((XWPFTableCell) tds.get(0), true);
					docTable.setCell(0, 0, cellContent);
				} else {
					int maxNumCells = numCells;
					for (int i = 1; i < numRows; ++i) {
						tr = table.getRow(i);
						numCells = tr.getTableICells().size();
						maxNumCells = (maxNumCells >= numCells) ? maxNumCells : numCells;
					}
					docTable = new WordTable(numRows, maxNumCells);
					for (int i = 0; i < numRows; ++i) {
						tr = table.getRow(i);
						tds = tr.getTableCells();
						numCells = tds.size();
						for (int j = 0; j < numCells; ++j) {
							WordArticle cellContent = new WordArticle();
							if (j < numCells) {
								cellContent = getCellContent(tr.getCell(j), false);
							}
							docTable.setCell(i, j, cellContent);
						}
					}
				}
				tables.add(docTable);
				elements.add(docTable);

			default:
				break;
			}
		}
	}

	private int getTitleLevel(XWPFParagraph paragraph) {
		CTPPr pPr = paragraph.getCTP().getPPr();
		if (pPr == null) {
			return 0;
		}
		CTDecimalNumber outlineLvl = pPr.getOutlineLvl();
		if (outlineLvl == null) {
			return 0;
		}
		int lvl = outlineLvl.getVal().intValue() + 1;
		if (lvl >= 0 && lvl < 6) {
			return lvl;
		}
		return 0;
	}

	private WordArticle getCellContent(XWPFTableCell tableCell, boolean isSingleBlock) {
		WordArticle cellArticle = new WordArticle();
		List<WordElement> cellElements = cellArticle.getElements();
		List<WordParagraph> cellParagraphs = cellArticle.getParagraphs();

		List<XWPFParagraph> xwpfParagraphs = tableCell.getParagraphs();
		for (XWPFParagraph paragraph : xwpfParagraphs) {
			String text = paragraph.getParagraphText();
			if (!"".equals(text.trim())) {
				text = isSingleBlock ? StringUtils.rtrim(text) : text.trim();
				if (isSingleBlock && (paragraph.getFirstLineIndent() >= 400)) {
					text = "\t" + text;
				}

				WordParagraph wordParagraph = new WordParagraph(text);
				cellParagraphs.add(wordParagraph);
				cellElements.add(wordParagraph);
			}
		}
		return cellArticle;
	}
}

class DocxPictureWrapper extends WordPicture {
	XWPFPictureData picture;

	DocxPictureWrapper(XWPFPictureData picture, boolean inList) {
		this.picture = picture;
		this.setInList(inList);
	}

	public byte[] getData() {
		return this.picture.getData();
	}

	public void writeTo(OutputStream out) throws IOException {
		out.write(getData());
	}
}
