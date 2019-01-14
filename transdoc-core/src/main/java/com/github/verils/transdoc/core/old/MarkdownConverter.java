package com.github.verils.transdoc.core.old;

import com.github.verils.transdoc.core.old.model.Article;
import com.github.verils.transdoc.core.old.model.Part;
import com.github.verils.transdoc.core.old.model.Paragraph;
import com.github.verils.transdoc.core.old.model.Picture;
import java.util.List;

import com.github.verils.transdoc.core.old.model.Table;

public class MarkdownConverter implements Convertor {

	@Override
	public String convert(Article article) {
		if (article == null) {
			return "";
		}

		List<Part> parts = article.getParts();
		StringBuilder markdown = new StringBuilder();

		for (Part part : parts) {
			boolean isInList = part.isInList();
			if (isInList) {
				markdown.append("\t");
			}

			switch (part.getType()) {
			case PARAGRAPH: {
				Paragraph paragraph = (Paragraph) part;
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
				Table table = (Table) part;
				StringBuilder tableContent = new StringBuilder();
				if (table.isBlock()) {
					Article cell = table.getCell(0, 0);
					tableContent.append("```").append("\n");
					for (Paragraph docParagraph : cell.getParagraphs()) {
						tableContent.append(docParagraph.getContent()).append("\n");
					}
					tableContent.append("```");
				} else {
					int rownum = table.getRownum();
					int colnum = table.getColnum();
					for (int i = 0; i < rownum; i++) {
						tableContent.append("|");
						for (int j = 0; j < colnum; j++) {
							Article cell = table.getCell(i, j);
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
				Picture picture = (Picture) part;
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

	private String convertCell(Article cell) {
		return convert(cell);
	}

	private String escapeHTML(String content) {
		content = content.replaceAll("&", "&amp;");
		content = content.replaceAll("<", "&lt;");
		content = content.replaceAll(">", "&gt;");
		return content;
	}

}