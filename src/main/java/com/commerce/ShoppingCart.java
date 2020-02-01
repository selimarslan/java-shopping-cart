package com.commerce;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private List<LineItem> items = new ArrayList<>();
    public void addItem(Product product, int quantity) {
        LineItem lineItem = new LineItem(product, quantity);
        this.items.add(lineItem);
    }

    public int getItemCount() {
        return items.size();
    }

    public void applyDiscounts(Campaign... campaigns) throws Exception {
        for (Campaign campaign: campaigns){
            for (LineItem item : items){
                if(item.getProduct().getCategory() == campaign.getCategory() && item.getQuantity() >= campaign.getLowerLimit()){
                    double campaignDiscount = campaign.getDiscountFactor() / 100 * item.getSubTotal();
                    item.setCampaignDiscount(campaign, campaignDiscount);
                }
            }
        }
    }



    public double getCampaignDiscount() {
        double result = 0;
        for (LineItem item : items){
            result += item.getCampaignDiscount();
        }
        return result;
    }

    public void applyCoupon(Coupon coupon) {

    }

    public double getCouponDiscount() {
        return 0;
    }
}

