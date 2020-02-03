package com.commerce.shared;

import com.commerce.category.Category;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class SharedTest {
    List<Category> categoryList;
    Category foodCategory, fruitCategory, vegyCategory;

    @Before
    public void setUp() throws Exception {
        categoryList = new ArrayList<>();
        foodCategory = new Category("Food");
        categoryList.add(foodCategory);
        fruitCategory = new Category("Fruit", foodCategory);
        categoryList.add(fruitCategory);
        vegyCategory = new Category("Vegetable", foodCategory);
        categoryList.add(vegyCategory);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetDistinctCategoriesWithHierarcy() {
        List<Category> expected = new ArrayList<>();
        expected.add(foodCategory);

        List<Category> distinctCategories = new DistinctCategoriesWithHierarchy().getDistinctCategories(categoryList);

        Assert.assertArrayEquals(expected.toArray(), distinctCategories.toArray());
    }

    @Test
    public void testgetDistinctCategoriesWithoutHierarchy(){
        List<Category> expected = new ArrayList<>();
        expected.add(foodCategory);
        expected.add(fruitCategory);
        expected.add(vegyCategory);

        List<Category> distinctCategories = new DistinctCategoriesWithoutHierarchy().getDistinctCategories(categoryList);

        Assert.assertArrayEquals(expected.toArray(), distinctCategories.toArray());
    }
}