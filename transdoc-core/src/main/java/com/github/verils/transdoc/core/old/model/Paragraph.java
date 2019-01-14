package com.github.verils.transdoc.core.model;

public class Paragraph extends Part {

    private String content;

    private Style style;

    public Paragraph(String content) {
        super(PartType.PARAGRAPH);
        this.content = content;
        this.style = new Style();
    }

    public Paragraph(String content, boolean inList) {
        this(content);
        this.setInList(inList);
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTitleLvl() {
        return style.getTitleLvl();
    }

    public void setTitleLvl(int titleLvl) {
        style.setTitleLvl(titleLvl);
    }

    public int getListLvl() {
        return style.getListLvl();
    }

    public void setListLvl(int listLvl) {
        style.setListLvl(listLvl);
    }

    public int hashCode() {
        return style.hashCode();
    }

    public boolean equals(Object obj) {
        return style.equals(obj);
    }


    private class Style {

        /**
         * 标题大纲级别
         */
        private int titleLvl;
        /**
         * 列表嵌套层次级别
         */
        private int listLvl;
        /**
         * 列表顺序
         */
        private int listIndex;

        public int getTitleLvl() {
            return this.titleLvl;
        }

        public void setTitleLvl(int titleLvl) {
            this.titleLvl = titleLvl;
        }

        public int getListLvl() {
            return this.listLvl;
        }

        public void setListLvl(int listLvl) {
            this.listLvl = listLvl;
        }

        public int getListIndex() {
            return listIndex;
        }

        public void setListIndex(int listIndex) {
            this.listIndex = listIndex;
        }
    }
}