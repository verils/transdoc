package com.github.verils.transdoc.core.model;

public interface PicturePart extends Part {

    int getId();

    String getName();

    String getExtension();

    int getDataOffset();

    byte[] getData();
}
