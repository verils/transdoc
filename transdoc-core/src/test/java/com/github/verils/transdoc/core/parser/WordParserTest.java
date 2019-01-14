package com.github.verils.transdoc.core;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import com.github.verils.transdoc.core.model.Article;
import java.io.IOException;
import java.io.InputStream;

import com.github.verils.transdoc.core.old.WordParserOld;
import com.github.verils.transdoc.core.parser.WordParser;
import org.junit.Test;

public class WordParserTest {

	@Test
	public void testParse() {
		InputStream input = WordParserTest.class.getResourceAsStream("/test.doc");
		WordParser parser = WordParser.parse(input);
		Article article = parser.getArticle();
	}

	@Test
	public void testPrepareForDoc() {
		doParse("/test.doc");
	}

	@Test
	public void testPrepareForDocx() {
		doParse("/test.docx");
	}

	private void doParse(String resource) {
		try {
			WordParserOld parser = WordParserOld.prepare(WordParserTest.class.getResourceAsStream(resource));
			Article article = parser.parse();
			assertNotNull(article);
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
}
