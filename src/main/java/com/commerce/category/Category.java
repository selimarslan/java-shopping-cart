package com.commerce.category;

public class Category{
    private Category parentCategory;
    String title;

    public Category(String title) {
        this(title, null);
    }

    public Category(String title, Category parentCategory){
        this.title = title;
        this.parentCategory = parentCategory;
    }

    public Category getParentCategory() {
        return parentCategory;
    }
}
