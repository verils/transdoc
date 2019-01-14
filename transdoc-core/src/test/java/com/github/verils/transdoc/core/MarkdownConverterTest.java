package com.github.verils.transdoc.core;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import com.github.verils.transdoc.core.model.Article;
import java.io.IOException;

import org.junit.Test;

public class MarkdownConverterTest {

	@Test
	public void testConvertForDoc() {
		doConvert("/测试.doc");
	}

	@Test
	public void testConvertForDocx() {
		doConvert("/测试.docx");
	}

	private void doConvert(String resource) {
		try {
			WordParser parser = WordParser.prepare(WordParserTest.class.getResourceAsStream(resource));
			parser.setSavePictures(true);
			Article article = parser.parse();

			Convertor convertor = new MarkdownConverter();
			String markdown = convertor.convert(article);
			assertNotNull(markdown);
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

}
