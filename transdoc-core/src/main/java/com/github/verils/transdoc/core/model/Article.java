package com.github.verils.transdoc.core.model;

import java.util.ArrayList;
import java.util.List;

public class Article {

    private final List<Part> parts;

    public Article() {
        parts = new ArrayList<>();
    }

    public List<Part> getParts() {
        return parts;
    }
}
