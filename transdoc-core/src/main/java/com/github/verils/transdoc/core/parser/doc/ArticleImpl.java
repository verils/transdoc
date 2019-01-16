package com.github.verils.transdoc.core.parser.doc;

import com.github.verils.transdoc.core.model.Article;
import com.github.verils.transdoc.core.model.Part;

import java.util.List;

public class ArticleImpl implements Article {

    private final List<Part> parts;

    public ArticleImpl(List<Part> parts) {
        this.parts = parts;
    }

    @Override
    public List<Part> getParts() {
        return parts;
    }
}
