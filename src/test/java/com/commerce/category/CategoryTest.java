package com.commerce.category;

import com.commerce.campaign.Campaign;
import com.commerce.cart.LineItem;
import com.commerce.cart.Product;
import com.commerce.shared.DiscountType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CategoryTest {
    Category fruitCategory = new Category("Fruit");
    Campaign fruitCampaign = new Campaign(fruitCategory, 15.0, 5, DiscountType.Rate);
    Campaign goldFruitCampaign = new Campaign(fruitCategory, 278.16, 8, DiscountType.Amount);
    Product apple = new Product("Apple", 100.0, fruitCategory);
    Product mango = new Product("Mango", 150.0, fruitCategory);

    @Test
    public void testCategories() {
        Category foodCategory = new Category("Food");
        Category vegetableCategory = new Category("Vegetable", foodCategory);
        Category fruitCategory = new Category("Fruit", foodCategory);
        Assert.assertEquals(foodCategory, fruitCategory.getParentCategory());
    }

    @Test
    public void testAddingItemsToWeightedCategoryWithoutAnyCampaigTheMaxCampaignAndMaxDiscountWillStaySame() {
        WeightedCategory weightedCategory = new WeightedCategory(fruitCategory);
        LineItem lineItem = new LineItem(apple, 4);
        weightedCategory.addLineItem(lineItem);
        weightedCategory.addLineItem(new LineItem(mango, 5));


        double maxDiscount = weightedCategory.getMaxDiscount();
        Campaign maxCampaign = weightedCategory.getMaxCampaign();


        Assert.assertEquals(0, maxDiscount, 0.1);
        Assert.assertEquals(null, maxCampaign);
    }

    @Test
    public void testAddingItemsToWeightedCategoryWithCampaignWillCalculateTheMaxCampaignAndMaxDiscount() {
        WeightedCategory weightedCategory = new WeightedCategory(fruitCategory);
        weightedCategory.addCampaign(fruitCampaign);
        weightedCategory.addCampaign(goldFruitCampaign);

        LineItem lineItem = new LineItem(apple, 4);
        weightedCategory.addLineItem(lineItem);
        weightedCategory.addLineItem(new LineItem(mango, 5));


        double maxDiscount = weightedCategory.getMaxDiscount();
        Campaign maxCampaign = weightedCategory.getMaxCampaign();


        Assert.assertEquals(278.16, maxDiscount, 0.1);
        Assert.assertEquals(goldFruitCampaign, maxCampaign);
    }

    @Test
    public void testCompareTo(){
        WeightedCategory firstOne = new WeightedCategory(fruitCategory);
        firstOne.addCampaign(fruitCampaign);
        WeightedCategory secondOne = new WeightedCategory(fruitCategory);
        secondOne.addCampaign(goldFruitCampaign);

        LineItem lineItem = new LineItem(apple, 9);
        firstOne.addLineItem(lineItem);
        secondOne.addLineItem(new LineItem(mango, 5));


        assertEquals(firstOne.compareTo(secondOne), -1);
        assertEquals(secondOne.compareTo(firstOne), 1);
        assertEquals(firstOne.compareTo(firstOne), 0);
    }

    @Test
    public void testDistributeDiscount() throws Exception {
        WeightedCategory weightedCategory = new WeightedCategory(fruitCategory);
        weightedCategory.addCampaign(fruitCampaign);
        weightedCategory.addCampaign(goldFruitCampaign);

        LineItem lineItem = new LineItem(apple, 13);
        weightedCategory.addLineItem(lineItem);


        weightedCategory.distributeDiscountsToLineItems();


        Assert.assertEquals(278.16, lineItem.getCampaignDiscount(), 0.1);
    }
}