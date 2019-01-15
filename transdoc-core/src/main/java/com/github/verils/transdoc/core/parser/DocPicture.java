package com.github.verils.transdoc.core.parser;

import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Picture;

public class DocPicture {

    private final CharacterRun characterRun;

    private final Picture picture;

    public DocPicture(CharacterRun characterRun, Picture picture) {
        this.characterRun = characterRun;
        this.picture = picture;
    }
}
