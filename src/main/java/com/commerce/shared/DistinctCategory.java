package com.commerce.shared;

import com.commerce.category.Category;

import java.util.Collection;
import java.util.List;

public interface DistinctCategory{
    List<Category> getDistinctCategories(Collection<Category> singleLevelCategories);
}
