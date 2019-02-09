package com.github.verils.transdoc.core.converter;

import com.github.verils.transdoc.core.model.WordDocument;

public interface Converter {

    String convert(WordDocument wordDocument);

    void setPictureDir(String pictureDir);
}
