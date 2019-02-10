package com.github.verils.transdoc.core.parser;

import com.github.verils.transdoc.core.model.PicturePart;

import java.util.Arrays;

public abstract class AbstractPicturePart implements PicturePart {

    private String name;
    private String extension;
    private byte[] data;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    @Override
    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "AbstractPicturePart{" +
            "name='" + name + '\'' +
            ", extension='" + extension + '\'' +
            ", data=" + Arrays.toString(data) +
            '}';
    }
}
