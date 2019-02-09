package com.github.verils.transdoc.core.parser;

import com.github.verils.transdoc.core.model.*;
import com.github.verils.transdoc.core.model.Part;
import com.github.verils.transdoc.core.model.PicturePart;

import java.util.List;

public class SimpleWordDocument implements WordDocument {

    private List<PicturePart> pictures;

    private List<TablePart> tables;

    private List<Part> parts;

    @Override
    public List<PicturePart> getPictures() {
        return pictures;
    }

    public void setPictures(List<PicturePart> pictures) {
        this.pictures = pictures;
    }

    @Override
    public List<TablePart> getTables() {
        return tables;
    }

    public void setTables(List<TablePart> tables) {
        this.tables = tables;
    }

    @Override
    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }
}
