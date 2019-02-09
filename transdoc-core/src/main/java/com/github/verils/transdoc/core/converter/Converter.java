package com.github.verils.transdoc.core.converter;

import com.github.verils.transdoc.core.model.WordDocument;

public interface Converter {

    String convert(WordDocument article);

    void setPictureDir(String pictureDir);
}
