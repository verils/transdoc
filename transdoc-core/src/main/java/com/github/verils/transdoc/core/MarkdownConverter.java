package com.github.verils.transdoc.core;

import com.github.verils.transdoc.core.model.WordArticle;
import com.github.verils.transdoc.core.model.WordElement;
import com.github.verils.transdoc.core.model.WordParagraph;
import com.github.verils.transdoc.core.model.WordPicture;
import java.util.List;

import com.github.verils.transdoc.core.model.WordTable;

public class MarkdownConverter implements Convertor {

	@Override
	public String convert(WordArticle article) {
		if (article == null) {
			return "";
		}

		List<WordElement> elements = article.getElements();
		StringBuilder markdown = new StringBuilder();

		for (WordElement wordElement : elements) {
			boolean isInList = wordElement.isInList();
			if (isInList) {
				markdown.append("\t");
			}

			switch (wordElement.getType()) {
			case PARAGRAPH: {
				WordParagraph paragraph = (WordParagraph) wordElement;
				String content = paragraph.getContent();
				int titleLvl = paragraph.getTitleLvl();
				int listLvl = paragraph.getListLvl();
				if (titleLvl > 0) {
					// 标题段落
					for (int i = 0; i < titleLvl; i++) {
						markdown.append("#");
					}
					markdown.append(" ");
				} else if (isInList || listLvl > 0) {
					// 列表段落
					markdown.append(listLvl).append(". ");
				}
				markdown.append(escapeHTML(content));
				break;
			}

			case TABLE: {
				WordTable table = (WordTable) wordElement;
				StringBuilder tableContent = new StringBuilder();
				if (table.isBlock()) {
					WordArticle cell = table.getCell(0, 0);
					tableContent.append("```").append("\n");
					for (WordParagraph docParagraph : cell.getParagraphs()) {
						tableContent.append(docParagraph.getContent()).append("\n");
					}
					tableContent.append("```");
				} else {
					int rownum = table.getRownum();
					int colnum = table.getColnum();
					for (int i = 0; i < rownum; i++) {
						tableContent.append("|");
						for (int j = 0; j < colnum; j++) {
							WordArticle cell = table.getCell(i, j);
							String cellContent = convertCell(cell);
							cellContent = cellContent.trim().replaceAll("\n", "<br>");
							tableContent.append(cellContent).append("|");
						}
						if (isInList) {
							tableContent.append("\n").append("\t");
						}
						// 添加表格第二行分隔行
						if (i == 0) {
							tableContent.append("\n").append("|");
							for (int j = 0; j < colnum; j++) {
								tableContent.append("----|");
							}
						}
						tableContent.append("\n");
					}
					// 删除最后的换行符
					tableContent.deleteCharAt(tableContent.length() - 1);
				}
				markdown.append(tableContent);
				break;
			}

			case PICTURE: {
				WordPicture picture = (WordPicture) wordElement;
				markdown.append("![](").append(picture.getRelativePath()).append(")");
				break;
			}

			default:
				break;
			}
			markdown.append("\n\n");
		}
		return markdown.toString();
	}

	private String convertCell(WordArticle cell) {
		return convert(cell);
	}

	private String escapeHTML(String content) {
		content = content.replaceAll("&", "&amp;");
		content = content.replaceAll("<", "&lt;");
		content = content.replaceAll(">", "&gt;");
		return content;
	}

}