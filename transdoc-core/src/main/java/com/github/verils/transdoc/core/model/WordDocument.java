package com.github.verils.transdoc.core.model;

import java.util.List;

public interface WordDocument {

    List<PictureEntry> getPictures();

    List<TableEntry> getTables();

    List<Entry> getEntries();
}
