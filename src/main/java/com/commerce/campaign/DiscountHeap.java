package com.commerce;

import java.util.PriorityQueue;

public class DiscountHeap {

    private PriorityQueue<WeightedCategory> heap = new PriorityQueue<>();

    public void addWeightedCategory(WeightedCategory weightedCategory) {
        this.heap.add(weightedCategory);
//        private Map<Category, WeightedCategory> categoryMap;
//        this.categoryMap.add(weightedCategory.getCategory(), weightedCategory);
    }

    public WeightedCategory getMaxDiscount() {
        return heap.peek();
    }

    public boolean removeWeightedCategory(WeightedCategory weightedCategory) {
        return heap.remove(weightedCategory);
    }

    public void mergeDiscountHeap(DiscountHeap other) {
        heap.addAll(other.heap);
//        for(WeightedCategory weightedCategory : other.heap){
//
//        }
    }
}
