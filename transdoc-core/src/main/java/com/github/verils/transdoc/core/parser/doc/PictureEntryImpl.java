package com.github.verils.transdoc.core.parser.doc;

import com.github.verils.transdoc.core.model.Part;
import com.github.verils.transdoc.core.model.PictureEntry;

public class PictureEntryImpl implements PictureEntry {

    private int id;

    private String name;

    private String extension;

    private int dataOffset;

    private byte[] data;

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
    public int getDataOffset() {
        return dataOffset;
    }

    public void setDataOffset(int dataOffset) {
        this.dataOffset = dataOffset;
    }

    @Override
    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
