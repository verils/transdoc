package com.github.verils.transdoc.core;

import com.github.verils.transdoc.core.converter.Converter;
import com.github.verils.transdoc.core.model.WordDocument;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransdocTest {

    private Transdoc transdoc;
    private InputStream docResource;
    private InputStream docxResource;

    @Before
    public void setUp() {
        Converter converter = mock(Converter.class);
        when(converter.convert(any(WordDocument.class))).thenReturn("test");

        transdoc = new Transdoc(converter);
        docResource = TransdocTest.class.getResourceAsStream("/test.doc");
        docxResource = TransdocTest.class.getResourceAsStream("/test.doc");
    }

    @Test
    public void testDocToStream() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        transdoc.transform(docResource, output);
        assertEquals("test\n", new String(output.toByteArray()));
    }

    @Test
    public void testDocxToStream() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        transdoc.transform(docxResource, output);
        assertEquals("test\n", new String(output.toByteArray()));
    }

    @Test
    public void testDocToWriter() {
        StringWriter writer = new StringWriter();
        transdoc.transform(docResource, writer);
        assertEquals("test\n", writer.toString());
    }

    @Test
    public void testDocxToWriter() {
        StringWriter writer = new StringWriter();
        transdoc.transform(docxResource, writer);
        assertEquals("test\n", writer.toString());
    }
}
