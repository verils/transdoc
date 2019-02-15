package com.github.verils.transdoc.core.converter;

import com.github.verils.transdoc.core.model.WordDocument;

import java.io.File;
import java.io.IOException;

public interface Converter {

    String convert(WordDocument wordDocument);

    String getPictureDir();

    void setPictureDir(String pictureDir);

    void extractPictures(WordDocument wordDocument, File dest) throws IOException;
}
