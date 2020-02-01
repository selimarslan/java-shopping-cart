package com.commerce;

import com.moodist.MoodAnalyser;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import javax.sound.sampled.Line;

public class ShoppingCartTest {
    @Test
    public void shouldCreateShoppingCart(){
        ShoppingCart shoppingCart = new ShoppingCart();
    }
    @Test
    public void testAddItem(){
        ShoppingCart shoppingCart = new ShoppingCart();
        Category foodCategory = new Category("Food");
        Product apple = new Product("Apple", 100.0, foodCategory);
        shoppingCart.addItem(apple, 10);
        int itemCount = shoppingCart.getItemCount();
        Assert.assertEquals(1, itemCount);
    }

    @Test
    public void testCategories() {
        Category foodCategory = new Category("Food");
        Category vegetableCategory = new Category("Vegetable", foodCategory);
        Category fruitCategory = new Category("Fruit", foodCategory);
        Assert.assertEquals(foodCategory, fruitCategory.getParentCategory());
    }

    @Test
    public void testZeroCampaignDiscount() throws Exception {
        ShoppingCart shoppingCart = new ShoppingCart();
        Category foodCategory = new Category("Food");
        Campaign campaign = new Campaign(foodCategory, 15.0, 5, DiscountType.Rate);
        Campaign campaignSecond = new Campaign(foodCategory, 55.0, 10, DiscountType.Amount);
        shoppingCart.applyDiscounts(campaign, campaignSecond);
        double campaignDiscount = shoppingCart.getCampaignDiscount();
        Assert.assertEquals(0, campaignDiscount, 0.1);
    }

    @Test
    public void testZeroCouponDiscount() {
        ShoppingCart shoppingCart = new ShoppingCart();
        Coupon coupon = new Coupon(100.0, 10.0, DiscountType.Rate);
        shoppingCart.applyCoupon(coupon);
        double couponDiscount = shoppingCart.getCouponDiscount();
        Assert.assertEquals(0, couponDiscount, 0.1);
    }

    @Test
    public void testLineItemSubTotal() throws Exception {
        Category foodCategory = new Category("Food");
        Product apple = new Product("Apple", 100.0, foodCategory);
        LineItem lineItem = new LineItem(apple, 3);
        Campaign campaign = new Campaign(foodCategory, 15.0, 5, DiscountType.Rate);
        lineItem.setCampaignDiscount(campaign, 35.23);
        lineItem.setCouponDiscount(12.23);
        double subTotal = lineItem.getSubTotal();
        Assert.assertEquals(252.54, subTotal, 0.1);
    }

    @Test
    public void testCampaignDiscountWithDifferentCategorySingleLevel() throws Exception {
        ShoppingCart shoppingCart = new ShoppingCart();

        Category fruitCategory = new Category("Fruit");
        Category vegetableCategory = new Category("Vegetable");

        Campaign fruitCampaign = new Campaign(fruitCategory, 15.0, 5, DiscountType.Rate);
        Campaign vegetableCampaign = new Campaign(vegetableCategory, 55.0, 10, DiscountType.Rate);

        Product apple = new Product("Apple", 100.0, fruitCategory);
        Product onion = new Product("Onion", 200.0, vegetableCategory);

        shoppingCart.addItem(apple, 5); // 500 75
        shoppingCart.addItem(onion, 10); // 2000 1100

        shoppingCart.applyDiscounts(fruitCampaign, vegetableCampaign);
        double campaignDiscount = shoppingCart.getCampaignDiscount();

        Assert.assertEquals(1175, campaignDiscount, 0.1);
    }

    @Test
    public void testCampaignDiscountWithSameCategorySingleLevel() throws Exception {
        ShoppingCart shoppingCart = new ShoppingCart();

        Category fruitCategory = new Category("Fruit");

        Campaign fruitCampaign = new Campaign(fruitCategory, 15.0, 5, DiscountType.Rate);

        Product apple = new Product("Apple", 100.0, fruitCategory);
        Product mango = new Product("Mango", 200.0, fruitCategory);

        shoppingCart.addItem(apple, 5); // 500 75
        shoppingCart.addItem(mango, 10); // 2000 300

        shoppingCart.applyDiscounts(fruitCampaign);
        double campaignDiscount = shoppingCart.getCampaignDiscount();

        Assert.assertEquals(375, campaignDiscount, 0.1);
    }

    @Test
    public void testCampaignDiscountWithParentCategory() throws Exception {
        ShoppingCart shoppingCart = new ShoppingCart();

        Category foodCategory = new Category("Food");
        Category fruitCategory = new Category("Fruit", foodCategory);
        Category vegetableCategory = new Category("Vegetable", foodCategory);

        Campaign fruitCampaign = new Campaign(fruitCategory, 15.0, 5, DiscountType.Rate);
        Campaign vegetableCampaign = new Campaign(vegetableCategory, 25.0, 10, DiscountType.Rate);
        Campaign foodCampaign = new Campaign(foodCategory, 700d, 15, DiscountType.Amount);

        Product apple = new Product("Apple", 100.0, fruitCategory);
        Product onion = new Product("Onion", 200.0, vegetableCategory);

        shoppingCart.addItem(apple, 5); // 500 75
        shoppingCart.addItem(onion, 10); // 2000 1100

        shoppingCart.applyDiscounts(fruitCampaign, vegetableCampaign, foodCampaign);
        double campaignDiscount = shoppingCart.getCampaignDiscount();

        // Assert.assertEquals(700, campaignDiscount, 0.1);
    }

    @Test
    public void testWeightedCategory() {
        Category fruitCategory = new Category("Fruit");
        Campaign fruitCampaign = new Campaign(fruitCategory, 15.0, 5, DiscountType.Rate);
        Campaign goldFruitCampaign = new Campaign(fruitCategory, 278.16, 8, DiscountType.Amount);
        Product apple = new Product("Apple", 100.0, fruitCategory);
        Product mango = new Product("Mango", 150.0, fruitCategory);

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
    public void testDiscountHeap() {
        DiscountHeap discountHeap = new DiscountHeap();
        // discountHeap.addWeightedCategory(weightedCategory);
    }

    @Test
    public void testUpdateWeightedCategoryToTheParent() {

        Category foodCategory = new Category("Food");
        Category fruitCategory = new Category("Fruit", foodCategory);
        Category vegetableCategory = new Category("Vegetable", foodCategory);

        Campaign fruitCampaign = new Campaign(fruitCategory, 15.0, 5, DiscountType.Rate);
        Campaign vegetableCampaign = new Campaign(vegetableCategory, 25.0, 10, DiscountType.Rate);
        Campaign foodCampaign = new Campaign(foodCategory, 700d, 15, DiscountType.Amount);

        Product apple = new Product("Apple", 100.0, fruitCategory);
        Product mango = new Product("Mango", 150.0, fruitCategory);
        Product onion = new Product("Onion", 200.0, vegetableCategory);

        CampaingDiscountManager campaingDiscountManager = new CampaingDiscountManager();

        // WeightedCategory weightedCategory = new WeightedCategory(apple);
    }
}
