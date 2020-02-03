package com.commerce.cart;

import com.commerce.campaign.Campaign;

import javax.print.attribute.Attribute;

public class LineItem{
    private Product product;
    private int quantity;
    private Campaign campaign;
    private double campaignDiscountAmount;
    private double couponDiscountAmount;

    public LineItem(Product product, int quantity){
        this.product = product;
        this.quantity = quantity;
    }

    public void setCampaignDiscount(Campaign campaign, double discountAmount) throws Exception {
        if(getSubTotal() < discountAmount){
            throw new Exception("Subtotal cannot be negative");
        }
        this.campaign = campaign;
        this.campaignDiscountAmount = discountAmount;
    }


    public void setCouponDiscount(double discountAmount) throws Exception {
        if(getSubTotal() < discountAmount){
            throw new Exception("Subtotal cannot be negative");
        }
        this.couponDiscountAmount = discountAmount;
    }

    public double getTotal(){
        double lineTotal = product.getPrice() * quantity;
        return lineTotal;
    }

    public double getTotalDiscount(){
        double lineTotalDiscount = campaignDiscountAmount + couponDiscountAmount;
        return lineTotalDiscount;
    }

    public double getSubTotal() {
        double lineTotal = product.getPrice() * quantity;
        double subTotal = lineTotal - campaignDiscountAmount - couponDiscountAmount;
        return subTotal;
    }

    public int getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public double getCampaignDiscount() {
        return campaignDiscountAmount;
    }

    public double getCouponDiscount() {
        return couponDiscountAmount;
    }

}
