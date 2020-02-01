package com.commerce;

class Product{
    private String title;
    private Double price;
    private Category category;

    Product(String title, Double price, Category category){
        this.title = title;
        this.price = price;
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public Category getCategory() {
        return category;
    }
}

