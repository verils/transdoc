package com.github.verils.transdoc.core.model;

public abstract class Part {

    public static enum Type {
        Title, Paragraph, Table, Picture;
    }

    private final Type type;


    public Part(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
