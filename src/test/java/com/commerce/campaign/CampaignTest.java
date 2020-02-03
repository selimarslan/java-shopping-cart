package com.commerce.campaign;

import com.commerce.cart.LineItem;
import com.commerce.cart.Product;
import com.commerce.category.Category;
import com.commerce.category.WeightedCategory;
import com.commerce.shared.DiscountType;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CampaignTest {

    WeightedCategory createWeightedCategory(String categoryTitle, String productTitle, double discount){
        Category category = new Category(categoryTitle);
        Campaign fruitCampaign = new Campaign(category, discount, 1, DiscountType.Rate);
        Product apple = new Product(productTitle, 100.0, category);
        WeightedCategory weightedCategory = new WeightedCategory(category);
        weightedCategory.addCampaign(fruitCampaign);
        weightedCategory.addLineItem(new LineItem(apple, 1));
        return weightedCategory;
    }

    @Test
    public void testDiscountHeap() {
        DiscountHeap discountHeap = new DiscountHeap();

        WeightedCategory fruitApple50 = createWeightedCategory("Fruit", "Apple", 50);
        WeightedCategory vegetableOnion30 = createWeightedCategory("Vegetable", "Onion", 30);
        WeightedCategory foodAppleAndOnion60 = createWeightedCategory("Food", "(Apple,Onion)", 60);
        WeightedCategory groceryFoodAndCoffe70 = createWeightedCategory("Grocery", "Food(Apple,Onion),Coffee", 70);


        discountHeap.addWeightedCategory(fruitApple50);
        WeightedCategory temp = discountHeap.getMaxDiscount();
        discountHeap.addWeightedCategory(groceryFoodAndCoffe70);
        discountHeap.addWeightedCategory(foodAppleAndOnion60);
        discountHeap.addWeightedCategory(vegetableOnion30);

        WeightedCategory maxDiscountBeforeRemove70 = discountHeap.getMaxDiscount();

        boolean b = discountHeap.removeWeightedCategory( groceryFoodAndCoffe70 );
        WeightedCategory maxDiscountAfterRemove70 = discountHeap.getMaxDiscount();

        Assert.assertEquals(50, fruitApple50.getMaxDiscount(), 0.0001);
        Assert.assertEquals(30, vegetableOnion30.getMaxDiscount(), 0.0001);
        Assert.assertEquals(groceryFoodAndCoffe70, maxDiscountBeforeRemove70);
        Assert.assertEquals(foodAppleAndOnion60, maxDiscountAfterRemove70);
    }

    @Test
    public void testCampaignDiscountManager(){

        Category groceryCategory = new Category("Grocery");
        Category foodCategory = new Category("Food", groceryCategory);
        Category beverageCategory = new Category("Beverage", groceryCategory);
        Category fruitCategory = new Category("Fruit", foodCategory);
        Category vegetableCategory = new Category("Vegetable", foodCategory);

        Category vehicleCategory = new Category("Vehicle");
        Category suvCategory = new Category("Suv", vehicleCategory);
        Category sedanCategory = new Category("Sedan", vehicleCategory);

        Category homeCategory = new Category("Home");

        Campaign fruitCampaign = new Campaign(fruitCategory, 15d, 5, DiscountType.Rate);
        Campaign fruitCampaignGold = new Campaign(fruitCategory, 17d, 10, DiscountType.Rate);
        Campaign vegetableCampaign = new Campaign(vegetableCategory, 20d, 10, DiscountType.Rate);
        Campaign beverageCampaign = new Campaign(beverageCategory, 100d, 10, DiscountType.Amount);
        Campaign foodCampaign = new Campaign(foodCategory, 452.23, 20, DiscountType.Amount);
        Campaign groceryCampaign = new Campaign(groceryCategory, 21d, 10, DiscountType.Rate);

        Campaign vehicleCampaign = new Campaign(vehicleCategory, 19d, 3, DiscountType.Rate);
        Campaign suvCampaign = new Campaign(suvCategory, 23d, 2, DiscountType.Rate);
        Campaign sedanCampaign = new Campaign(sedanCategory, 18d, 4, DiscountType.Rate);

        Campaign homeCampaign = new Campaign(homeCategory, 12d, 2, DiscountType.Rate);


        Campaign[] campaigns = new Campaign[10];
        campaigns[0] = fruitCampaign;
        campaigns[1] = vegetableCampaign;
        campaigns[2] = beverageCampaign;
        campaigns[3] = foodCampaign;
        campaigns[4] = fruitCampaignGold;
        campaigns[5] = groceryCampaign;

        campaigns[6] = vehicleCampaign;
        campaigns[7] = suvCampaign;
        campaigns[8] = sedanCampaign;

        campaigns[9] = homeCampaign;

        Product apple = new Product("Apple", 100.0, fruitCategory);
        LineItem appleItem = new LineItem(apple, 5);

        Product mango = new Product("Mango", 150.0, fruitCategory);
        LineItem mangoItem = new LineItem(mango, 15);

        Product onion = new Product("Onion", 40.0, foodCategory);
        LineItem onionItem = new LineItem(onion, 10);

        Product potato = new Product("Potato", 52.0, vegetableCategory);
        LineItem potatoItem = new LineItem(potato, 8);

        Product corolla = new Product("Corrolla", 1340.0, sedanCategory);
        LineItem corollaItem = new LineItem(corolla, 5);

        Product kodiaq = new Product("Kodiaq", 1810.0, suvCategory);
        LineItem kodiaqItem = new LineItem(kodiaq, 3);

        Product home = new Product("Home", 32382.0, homeCategory);
        LineItem homeItem = new LineItem(home, 3);

        CampaingDiscountManager campaingDiscountManager = new CampaingDiscountManager(campaigns);
        campaingDiscountManager.addItem(appleItem);
        campaingDiscountManager.addItem(mangoItem);
        campaingDiscountManager.addItem(onionItem);
        campaingDiscountManager.addItem(potatoItem);

        campaingDiscountManager.addItem(corollaItem);
        campaingDiscountManager.addItem(kodiaqItem);
        campaingDiscountManager.addItem(homeItem);

        List<WeightedCategory> weightedCategories = campaingDiscountManager.getMaxDiscount();
        double maxDiscount = 0;
        for (WeightedCategory weightedCategory: weightedCategories){
            maxDiscount += weightedCategory.getMaxDiscount();
        }
        Assert.assertEquals(14711.08, maxDiscount, 0.001);
    }
}