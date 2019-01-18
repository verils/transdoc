package com.github.verils.transdoc.core.parser;

import com.github.verils.transdoc.core.model.Entry;
import com.github.verils.transdoc.core.model.PictureEntry;
import com.github.verils.transdoc.core.model.TableEntry;
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

        List<PictureEntry> pictures = document.getPictures();
        assertTrue(pictures.isEmpty());

        List<TableEntry> tables = document.getTables();
        assertEquals(8, tables.size());

        List<Entry> entries = document.getEntries();
        assertFalse(entries.isEmpty());

        for (Entry entry : entries) {
            System.out.println(entry.toString());
            assertNotNull(entry);
        }
    }
}
