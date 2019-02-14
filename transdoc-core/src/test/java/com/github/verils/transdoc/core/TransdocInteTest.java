package com.github.verils.transdoc.core;

import com.github.verils.transdoc.core.converter.MarkdownConverter;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

public class TransdocInteTest {

    private Transdoc transdoc;
    private InputStream docResource;
    private InputStream docxResource;

    @Before
    public void setUp() {
        transdoc = new Transdoc(new MarkdownConverter());
        docResource = TransdocTest.class.getResourceAsStream("/test.doc");
        docxResource = TransdocTest.class.getResourceAsStream("/test.docx");
    }

    @Test
    public void testDoc() {
        transdoc.transform(docResource, System.out);
    }

    @Test
    public void testDocx() {
        transdoc.transform(docxResource, System.out);
    }
}
