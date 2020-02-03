package com.commerce.cart;

import com.commerce.campaign.Campaign;
import com.commerce.category.Category;
import com.commerce.coupon.Coupon;
import com.commerce.shared.DiscountType;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ShoppingCartTest {
    Category foodCategory = new Category("Food");
    Category fruitCategory = new Category("Fruit", foodCategory);
    Category vegetableCategory = new Category("Vegetable", foodCategory);
    Category bakeryCategory = new Category("Bakery");

    ShoppingCart cart = new ShoppingCart();

    @Test
    public void shouldCreateShoppingCart(){
        ShoppingCart shoppingCart = new ShoppingCart();
    }

    @Test
    public void testAddItem() throws Exception {
        Product apple = new Product("Apple", 100.0, foodCategory);


        cart.addItem(apple, 10);
        int itemCount = cart.getItemCount();


        Assert.assertEquals(1, itemCount);
    }



    @Test
    public void testZeroCampaignDiscount() throws Exception {
        Campaign campaign = new Campaign(foodCategory, 15.0, 5, DiscountType.Rate);
        Campaign campaignSecond = new Campaign(foodCategory, 55.0, 10, DiscountType.Amount);


        cart.applyDiscounts(campaign, campaignSecond);
        double campaignDiscount = cart.getCampaignDiscount();


        Assert.assertEquals(0, campaignDiscount, 0.1);
    }

    @Test
    public void testNoCouponDiscount() {

        double couponDiscount = cart.getCouponDiscount();


        Assert.assertEquals(0, couponDiscount, 0.1);
    }

    @Test
    public void testLineItemSubTotal() throws Exception {
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
        Campaign fruitCampaign = new Campaign(fruitCategory, 15.0, 5, DiscountType.Rate);
        Campaign bakeryCampaign = new Campaign(bakeryCategory, 55.0, 10, DiscountType.Rate);

        Product apple = new Product("Apple", 100.0, fruitCategory);
        Product onion = new Product("Cake", 200.0, bakeryCategory);

        cart.addItem(apple, 5); // 500 75
        cart.addItem(onion, 10); // 2000 1100


        cart.applyDiscounts(fruitCampaign, bakeryCampaign);
        double campaignDiscount = cart.getCampaignDiscount();


        Assert.assertEquals(1175, campaignDiscount, 0.1);
    }

    @Test
    public void testCampaignDiscountWithSameCategorySingleLevel() throws Exception {
        Category fruitCategory = new Category("Fruit");

        Campaign fruitCampaign = new Campaign(fruitCategory, 15.0, 5, DiscountType.Rate);

        Product apple = new Product("Apple", 100.0, fruitCategory);
        Product mango = new Product("Mango", 200.0, fruitCategory);

        cart.addItem(apple, 5); // 500 75
        cart.addItem(mango, 10); // 2000 300


        cart.applyDiscounts(fruitCampaign);
        double campaignDiscount = cart.getCampaignDiscount();


        Assert.assertEquals(375, campaignDiscount, 0.1);

    }

    @Test
    public void testCampaignDiscountWithParentCategory() throws Exception {
        Campaign fruitCampaign = new Campaign(fruitCategory, 15.0, 5, DiscountType.Rate);
        Campaign vegetableCampaign = new Campaign(vegetableCategory, 25.0, 10, DiscountType.Rate);
        Campaign foodCampaign = new Campaign(foodCategory, 700d, 15, DiscountType.Amount);

        Product apple = new Product("Apple", 100.0, fruitCategory);
        Product onion = new Product("Onion", 200.0, vegetableCategory);

        cart.addItem(apple, 5); // 500 75
        cart.addItem(onion, 10); // 2000 1100


        cart.applyDiscounts(fruitCampaign, vegetableCampaign, foodCampaign);
        double campaignDiscount = cart.getCampaignDiscount();


        Assert.assertEquals(700, campaignDiscount, 0.1);

    }

    @Test
    public void testCouponDiscount() throws Exception {
        Product apple = new Product("Apple", 100.0, fruitCategory);
        Product mango = new Product("Mango", 150.0, fruitCategory);
        Product onion = new Product("Onion", 40.0, vegetableCategory);

        Campaign fruitCampaign = new Campaign(fruitCategory, 20d, 15, DiscountType.Rate);
        Coupon coupon = new Coupon(2000.0, 10.0, DiscountType.Rate);

        cart.addItem(apple, 5);
        cart.addItem(mango, 15);
        cart.addItem(onion, 10);


        cart.applyDiscounts(fruitCampaign); // 3150 - 2600
        cart.applyCoupon(coupon);
        double couponDiscount = cart.getCouponDiscount();


        Assert.assertEquals(260, couponDiscount, 0.001);
    }

    @Test
    public void testApplyCouponWithLowerAmount() throws Exception {
        Product apple = new Product("Apple", 100.0, fruitCategory);
        Coupon coupon = new Coupon(2000.0, 10.0, DiscountType.Rate);
        cart.addItem(apple, 10);


        cart.applyCoupon(coupon);
        double couponDiscount = cart.getCouponDiscount();


        Assert.assertEquals(0, couponDiscount, 0.001);

    }

    @Test
    public void shouldPrintWithoutError() throws Exception {
        testCouponDiscount();


        cart.print();
    }

}
