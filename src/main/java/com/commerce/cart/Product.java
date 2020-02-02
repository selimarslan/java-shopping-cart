package com.commerce.cart;

import com.commerce.category.Category;

import java.util.Objects;

public class Product{
    private String title;
    private Double price;
    private Category category;

    public Product(String title, Double price, Category category){
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

