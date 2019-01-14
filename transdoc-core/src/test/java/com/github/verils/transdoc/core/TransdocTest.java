package com.github.verils.transdoc.core;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransdocTest {

    private Formatter formatter = mock(Formatter.class);

    @Before
    public void setUp() {
        when(formatter.format(any())).thenReturn("test");
    }

    @Test
    public void testDocToStream() throws IOException {

        InputStream input = TransdocTest.class.getResourceAsStream("/test.doc");
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Transdoc.parse(formatter, input, output);
        assertEquals("test\n", new String(output.toByteArray()));
    }

    @Test
    public void testDocxToStream() throws IOException {
        InputStream input = TransdocTest.class.getResourceAsStream("/test.docx");
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Transdoc.parse(formatter, input, output);
        assertEquals("test\n", new String(output.toByteArray()));
    }

    @Test
    public void testDocToWriter() throws IOException {
        InputStream input = TransdocTest.class.getResourceAsStream("/test.doc");
        StringWriter writer = new StringWriter();
        Transdoc.parse(formatter, input, writer);
        assertEquals("test\n", writer.toString());
    }

    @Test
    public void testDocxToWriter() throws IOException {
        InputStream input = TransdocTest.class.getResourceAsStream("/test.docx");
        StringWriter writer = new StringWriter();
        Transdoc.parse(formatter, input, writer);
        assertEquals("test\n", writer.toString());
    }
}
