package com.github.verils.transdoc.core.parser;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class WordParserTest {

    @Test
    public void testParseIllegalDoc() throws IOException {
        InputStream input = WordParserTest.class.getResourceAsStream("/test");
        WordParser parser = WordParser.parse(input);
    }

    @Test
    public void testParseDoc() throws IOException {
        InputStream input = WordParserTest.class.getResourceAsStream("/test.doc");
        WordParser parser = WordParser.parse(input);
        assertTrue(parser instanceof DocParser);
    }

    @Test
    public void testParseDocx() throws IOException {
        InputStream input = WordParserTest.class.getResourceAsStream("/test.docx");
        WordParser parser = WordParser.parse(input);
        assertTrue(parser instanceof DocxParser);
    }
}
