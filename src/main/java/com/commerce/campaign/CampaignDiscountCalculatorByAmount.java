package com.commerce.campaign;

import com.commerce.campaign.CampaignDiscountCalculator;

public class CampaignDiscountCalculatorByAmount implements CampaignDiscountCalculator {

    @Override
    public double calculateDiscount(Campaign campaign, double totalAmount) {
        return campaign.getDiscountFactor();
    }
}
