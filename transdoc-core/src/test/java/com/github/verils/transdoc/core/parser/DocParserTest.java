package com.github.verils.transdoc.core.parser;

import com.github.verils.transdoc.core.model.Article;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class DocParserTest {

    @Test
    public void getArticle() throws IOException {
        InputStream input = DocParserTest.class.getResourceAsStream("/test.doc");
        DocParser docParser = new DocParser(input);
        Article article = docParser.getArticle();
        assertFalse(article.getParts().isEmpty());
    }
}