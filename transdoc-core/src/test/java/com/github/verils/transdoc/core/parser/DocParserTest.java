package com.github.verils.transdoc.core.parser;

import com.github.verils.transdoc.core.model.Part;
import com.github.verils.transdoc.core.model.PicturePart;
import com.github.verils.transdoc.core.model.TablePart;
import com.github.verils.transdoc.core.model.WordDocument;
import com.github.verils.transdoc.core.parser.doc.WordDocumentImpl;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.*;

public class DocParserTest {

    private DocParser docParser;

    @Before
    public void setUp() {
        InputStream resource = DocParserTest.class.getResourceAsStream("/test.doc");
        docParser = new DocParser(resource);
    }

    @Test
    public void parse() throws IOException {
        WordDocument document = docParser.parse();
        assertTrue(document instanceof WordDocumentImpl);

        List<PicturePart> pictures = document.getPictures();
        assertTrue(pictures.isEmpty());

        List<TablePart> tables = document.getTables();
        assertEquals(8, tables.size());

        List<Part> parts = document.getParts();
        assertFalse(parts.isEmpty());

        for (Part part : parts) {
            System.out.println(part);
            assertNotNull(part);
        }
    }
}
