package com.github.verils.transdoc.core.converter;

import com.github.verils.transdoc.core.model.WordDocument;

import java.io.File;

public interface Converter {

    String convert(WordDocument wordDocument);

    void extractPictures(WordDocument wordDocument, File file);

    String getPictureDir();

    void setPictureDir(String pictureDir);
}
