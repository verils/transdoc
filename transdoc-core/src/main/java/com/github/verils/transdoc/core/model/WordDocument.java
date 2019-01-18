package com.github.verils.transdoc.core.model;

import java.util.List;

public interface WordDocument {

    List<PicturePart> getPictures();

    List<TablePart> getTables();

    List<Part> getParts();
}
