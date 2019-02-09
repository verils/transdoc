package com.github.verils.transdoc.core.parser.doc;

import com.github.verils.transdoc.core.model.Part;
import com.github.verils.transdoc.core.model.TableCellPart;
import com.github.verils.transdoc.core.parser.AbstractTableCellPart;

import java.util.List;

class DocTableCellPart extends AbstractTableCellPart {

    DocTableCellPart(List<Part> parts) {
        super(parts);
    }
}
