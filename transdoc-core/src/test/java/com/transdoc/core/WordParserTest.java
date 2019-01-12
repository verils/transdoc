package com.transdoc.core;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import com.transdoc.core.model.WordArticle;

public class WordParserTest {

	@Test
	public void testPrepareForDoc() {
		doParse("/测试.doc");
	}

	@Test
	public void testPrepareForDocx() {
		doParse("/测试.docx");
	}

	private void doParse(String resource) {
		try {
			WordParser parser = WordParser.prepare(WordParserTest.class.getResourceAsStream(resource));
			WordArticle article = parser.parse();
			assertNotNull(article);
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
}
