package com.github.verils.transdoc.core.model;

public abstract class Part {

    private final PartType type;

    private boolean inList;

    Part(PartType type) {
        this.type = type;
    }

    public PartType getType() {
        return type;
    }

    public boolean isInList() {
        return inList;
    }

    public void setInList(boolean inList) {
        this.inList = inList;
    }

}