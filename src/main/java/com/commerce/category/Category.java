package com.commerce;

class Category{
    Category parentCategory;
    String title;

    Category(String title) {
        this(title, null);
    }

    Category(String title, Category parentCategory){
        this.title = title;
        this.parentCategory = parentCategory;
    }

    public Category getParentCategory() {
        return parentCategory;
    }
}
