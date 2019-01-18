package com.github.verils.transdoc.core;

import com.github.verils.transdoc.core.model.WordDocument;

public interface Converter {

    String convert(WordDocument article);
}
