package com.github.verils.transdoc.core.old;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import com.github.verils.transdoc.core.old.model.Article;
import java.io.IOException;

import com.github.verils.transdoc.core.old.Convertor;
import com.github.verils.transdoc.core.old.MarkdownConverter;
import com.github.verils.transdoc.core.old.WordParserOld;
import com.github.verils.transdoc.core.parser.WordParserTest;
import org.junit.Test;

public class MarkdownConverterTest {

	@Test
	public void testConvertForDoc() {
		doConvert("/test.doc");
	}

	@Test
	public void testConvertForDocx() {
		doConvert("/test.docx");
	}

	private void doConvert(String resource) {
		try {
			WordParserOld parser = WordParserOld.prepare(WordParserTest.class.getResourceAsStream(resource));
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
