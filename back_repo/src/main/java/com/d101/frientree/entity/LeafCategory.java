package com.d101.frientree.entity;

import com.d101.frientree.exception.leaf.CategoryNotFoundException;
import lombok.Getter;

@Getter
public enum LeafCategory {
    ENCOURAGEMENT(1),
    COMFORT(2),
    FREEDOM(3);

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
        // 존재하지 않는 카테고리를 선택했을 때 null로 처리
        return null;
    }
}