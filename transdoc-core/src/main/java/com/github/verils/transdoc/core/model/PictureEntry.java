package com.github.verils.transdoc.core.model;

public interface PictureEntry extends Entry {

    int getId();

    String getName();

    String getExtension();

    int getDataOffset();

    byte[] getData();
}
