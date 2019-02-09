package com.github.verils.transdoc.core.parser.docx;

import com.github.verils.transdoc.core.model.Part;
import com.github.verils.transdoc.core.model.PicturePart;
import com.github.verils.transdoc.core.model.TablePart;
import com.github.verils.transdoc.core.model.WordDocument;
import com.github.verils.transdoc.core.parser.SimpleWordDocument;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.*;

public class DocxParserTest {

    @Test
    @Ignore
    public void testParse() throws IOException {
        InputStream input = DocxParserTest.class.getResourceAsStream("/test.docx");
        WordDocument document = new DocxParser(input).parse();
        assertTrue(document instanceof SimpleWordDocument);

        List<PicturePart> pictures = document.getPictures();
        assertEquals(4, pictures.size());

        List<TablePart> tables = document.getTables();
        assertEquals(8, tables.size());

        List<Part> parts = document.getParts();
        assertFalse(parts.isEmpty());

        for (Part part : parts) {
//            System.out.println(part);
            assertNotNull(part);
        }
    }
}
