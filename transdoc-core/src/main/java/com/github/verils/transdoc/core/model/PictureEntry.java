package com.github.verils.transdoc.core.model;

public interface PictureEntry extends Part {

    int getId();

    String getName();

    String getExtension();

    int getDataOffset();

    byte[] getData();

}
