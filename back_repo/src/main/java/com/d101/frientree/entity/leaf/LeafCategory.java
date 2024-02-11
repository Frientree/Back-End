package com.d101.frientree.entity.leaf;

import com.d101.frientree.exception.leaf.CategoryNotFoundException;
import lombok.Getter;

@Getter
public enum LeafCategory {
    ENCOURAGEMENT(1),
    COMFORT(2),
    FREEDOM(0);

    private final int value;

    LeafCategory(int value) {
        this.value = value;
    }

    public static LeafCategory findByValue(int value) {
        for (LeafCategory category : values()) {
            if (category.getValue() == value) {
                return category;
            }
        }
        throw new CategoryNotFoundException("Category not found");
    }
}