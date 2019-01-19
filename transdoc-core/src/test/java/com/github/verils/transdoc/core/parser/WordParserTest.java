package com.github.verils.transdoc.core.parser;

import com.github.verils.transdoc.core.model.WordDocument;
import com.github.verils.transdoc.core.parser.doc.WordDocumentImpl;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertTrue;

public class WordParserTest {

    @Test
    public void testParseDoc() {
        InputStream input = WordParserTest.class.getResourceAsStream("/test.doc");
        WordDocument document = WordParser.parseDocument(input);
        assertTrue(document instanceof WordDocumentImpl);
    }

//    @Test
//    public void testParseDocx() throws IOException {
//        InputStream input = WordParserTest.class.getResourceAsStream("/test.docx");
//        WordParser parser = WordParser.parseDocument(input);
//        assertTrue(parser instanceof DocxParser);
//    }

    @Test(expected = RuntimeException.class)
    public void testParseIllegalDoc() {
        InputStream input = WordParserTest.class.getResourceAsStream("/test");
        WordParser.parseDocument(input);
    }
}
