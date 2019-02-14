package com.github.verils.transdoc.core.model;

public interface PicturePart extends Part {

    int getIndex();

    String getName();

    String getExtension();

    byte[] getData();
}
