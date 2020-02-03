package com.commerce.cart;

import com.commerce.campaign.Campaign;
import com.commerce.campaign.CampaingDiscountManager;
import com.commerce.category.WeightedCategory;
import com.commerce.coupon.Coupon;
import com.commerce.coupon.CouponDiscountCalculator;
import com.commerce.coupon.CouponDiscountCalculatorByAmount;
import com.commerce.coupon.CouponDiscountCalculatorByRate;
import com.commerce.shared.DiscountType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {
    private List<LineItem> items = new ArrayList<>();
    private int totalQuanity = 0;
    private double totalAmount = 0;
    private Map<DiscountType, CouponDiscountCalculator> couponDiscountCalculators;

    public ShoppingCart(){
        this.couponDiscountCalculators = new HashMap<>();
        this.couponDiscountCalculators.put(DiscountType.Amount, new CouponDiscountCalculatorByAmount());
        this.couponDiscountCalculators.put(DiscountType.Rate, new CouponDiscountCalculatorByRate());
    }

    public void addItem(Product product, int quantity) throws Exception {
        LineItem lineItem = new LineItem(product, quantity);

        this.items.add(lineItem);
        this.totalQuanity += quantity;
        this.totalAmount += lineItem.getTotal();
    }

    public int getItemCount() {
        return items.size();
    }

    public void applyDiscounts(Campaign... campaigns) throws Exception {
        CampaingDiscountManager campaingDiscountManager = new CampaingDiscountManager(campaigns);
        for (LineItem item: items){
            campaingDiscountManager.addItem(item);
        }
        List<WeightedCategory> weightedCategories = campaingDiscountManager.getMaxDiscount();
        double maxDiscount = 0;
        for (WeightedCategory weightedCategory: weightedCategories){
            weightedCategory.distributeDiscountsToLineItems();
        }
    }

    public double getCampaignDiscount() {
        double result = 0;
        for (LineItem item : items){
            result += item.getCampaignDiscount();
        }
        return result;
    }

    public void applyCoupon(Coupon coupon) throws Exception {
        if( totalAmount < coupon.getMinPurchaseAmount() ){
            return;
        }

        double couponDiscount = 0;
        CouponDiscountCalculator couponDiscountCalculator = couponDiscountCalculators.get(coupon.getDiscountType());
        couponDiscount = couponDiscountCalculator.calculateDiscount(this,coupon);

        double unitDiscount = couponDiscount / totalAmount;
        for (LineItem item: items){
            item.setCouponDiscount(unitDiscount * item.getTotal());
        }
    }

    public double getCouponDiscount() {
        double result = 0;
        for (LineItem item : items){
            result += item.getCouponDiscount();
        }
        return result;
    }

    public List<LineItem> getItems() {
        return items;
    }

    public double getTotalAmount(){
        return totalAmount;
    }
}

