package com.github.verils.transdoc.core.parser.doc;

import com.github.verils.transdoc.core.model.Entry;
import com.github.verils.transdoc.core.model.PictureEntry;
import com.github.verils.transdoc.core.model.TableEntry;
import com.github.verils.transdoc.core.model.WordDocument;

import java.util.List;

public class WordDocumentImpl implements WordDocument {

    private List<PictureEntry> pictures;

    private List<TableEntry> tables;

    private List<Entry> entries;

    @Override
    public List<PictureEntry> getPictures() {
        return pictures;
    }

    public void setPictures(List<PictureEntry> pictures) {
        this.pictures = pictures;
    }

    @Override
    public List<TableEntry> getTables() {
        return tables;
    }

    public void setTables(List<TableEntry> tables) {
        this.tables = tables;
    }

    @Override
    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }
}
