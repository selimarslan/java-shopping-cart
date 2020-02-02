package com.commerce.campaign;

import com.commerce.campaign.CampaignDiscountCalculator;

public class CampaignDiscountCalculatorByRate implements CampaignDiscountCalculator {

    @Override
    public double calculateDiscount(Campaign campaign, double totalAmount) {
        double campaignDiscount = campaign.getDiscountFactor() / 100 * totalAmount;
        return campaignDiscount;
    }
}
