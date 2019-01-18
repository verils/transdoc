package com.github.verils.transdoc.core.parser.doc;

import com.github.verils.transdoc.core.model.*;
import com.github.verils.transdoc.core.model.Part;
import com.github.verils.transdoc.core.model.PicturePart;

import java.util.List;

public class WordDocumentImpl implements WordDocument {

    private List<PicturePart> pictures;

    private List<TablePart> tables;

    private List<Part> entries;

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
    public List<Part> getEntries() {
        return entries;
    }

    public void setEntries(List<Part> entries) {
        this.entries = entries;
    }
}