package com.github.verils.transdoc.core;

import com.github.verils.transdoc.core.model.Article;
import com.github.verils.transdoc.core.model.Part;
import com.github.verils.transdoc.core.model.Paragraph;
import com.github.verils.transdoc.core.model.Picture;
import com.github.verils.transdoc.core.model.Table;
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

	public Article parse() {
		Article article = new Article();
		parseDocument(article);
		return article;
	}

	private void parseDocument(Article article) {
		List<Part> parts = article.getParts();
		List<Paragraph> paragraphs = article.getParagraphs();
		List<Table> tables = article.getTables();
		List<Picture> pictures = article.getPictures();

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

							Picture wordPicture = new DocxPictureWrapper(pictureData, inList);
							wordPicture.setIndex(pictures.size() + 1);
							wordPicture.setPictureName(pictureData.getFileName());
							wordPicture.setExtension(pictureData.suggestFileExtension());
							wordPicture.setRelativePath(getImagePath(wordPicture));
							pictures.add(wordPicture);
							parts.add(wordPicture);
						}
					}
				}

				String text = paragraph.getParagraphText().trim();
				if (!paragraph.isEmpty() && !"".equals(text)) {
					Paragraph wordParagraph = new Paragraph(text);
					wordParagraph.setTitleLvl(getTitleLevel(paragraph));
					wordParagraph.setInList(inList);
					paragraphs.add(wordParagraph);
					parts.add(wordParagraph);
				}
				break;

			case TABLE:
				XWPFTable table = (XWPFTable) bodyElement;
				Table docTable = null;

				int numRows = table.getNumberOfRows();
				XWPFTableRow tr = table.getRow(0);
				List<XWPFTableCell> tds = tr.getTableCells();
				int numCells = tds.size();
				if ((numRows == 1) && (numCells == 1)) {
					docTable = new Table(numRows, numCells);
					docTable.setInList(isInList);
					Article cellContent = getCellContent((XWPFTableCell) tds.get(0), true);
					docTable.setCell(0, 0, cellContent);
				} else {
					int maxNumCells = numCells;
					for (int i = 1; i < numRows; ++i) {
						tr = table.getRow(i);
						numCells = tr.getTableICells().size();
						maxNumCells = (maxNumCells >= numCells) ? maxNumCells : numCells;
					}
					docTable = new Table(numRows, maxNumCells);
					for (int i = 0; i < numRows; ++i) {
						tr = table.getRow(i);
						tds = tr.getTableCells();
						numCells = tds.size();
						for (int j = 0; j < numCells; ++j) {
							Article cellContent = new Article();
							if (j < numCells) {
								cellContent = getCellContent(tr.getCell(j), false);
							}
							docTable.setCell(i, j, cellContent);
						}
					}
				}
				tables.add(docTable);
				parts.add(docTable);

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

	private Article getCellContent(XWPFTableCell tableCell, boolean isSingleBlock) {
		Article cellArticle = new Article();
		List<Part> cellParts = cellArticle.getParts();
		List<Paragraph> cellParagraphs = cellArticle.getParagraphs();

		List<XWPFParagraph> xwpfParagraphs = tableCell.getParagraphs();
		for (XWPFParagraph paragraph : xwpfParagraphs) {
			String text = paragraph.getParagraphText();
			if (!"".equals(text.trim())) {
				text = isSingleBlock ? StringUtils.rtrim(text) : text.trim();
				if (isSingleBlock && (paragraph.getFirstLineIndent() >= 400)) {
					text = "\t" + text;
				}

				Paragraph wordParagraph = new Paragraph(text);
				cellParagraphs.add(wordParagraph);
				cellParts.add(wordParagraph);
			}
		}
		return cellArticle;
	}
}

class DocxPictureWrapper extends Picture {
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
