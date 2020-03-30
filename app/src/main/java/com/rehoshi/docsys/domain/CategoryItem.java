package com.rehoshi.docsys.domain;

public class CategoryItem {
    private int color ;
    private String title ;
    private String category ;

    public CategoryItem() {
    }

    public CategoryItem(int color, String title, String category) {
        this.color = color;
        this.title = title;
        this.category = category;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
