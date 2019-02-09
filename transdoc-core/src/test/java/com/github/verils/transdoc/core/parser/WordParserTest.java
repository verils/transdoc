package com.github.verils.transdoc.core.parser;

import com.github.verils.transdoc.core.model.WordDocument;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertTrue;

public class WordParserTest {

    @Test
    public void testParseDoc() {
        InputStream input = WordParserTest.class.getResourceAsStream("/test.doc");
        WordDocument document = WordParser.parseDocument(input);
        assertTrue(document instanceof SimpleWordDocument);
    }

    @Test
    public void testParseDocx() {
        InputStream input = WordParserTest.class.getResourceAsStream("/test.docx");
        WordDocument document = WordParser.parseDocument(input);
        assertTrue(document instanceof SimpleWordDocument);
    }

    @Test(expected = RuntimeException.class)
    public void testParseIllegalDoc() {
        InputStream input = WordParserTest.class.getResourceAsStream("/test");
        WordParser.parseDocument(input);
    }
}
