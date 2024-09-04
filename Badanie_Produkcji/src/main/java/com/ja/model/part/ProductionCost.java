package com.ja.model.part;

import com.ja.model.dto.ProductionDto;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProductionCost {

    private Map<Integer, Integer> production;
    private Set<Integer> keys;
    private int maxValue;

    public ProductionCost() {

        production = new HashMap<>();

        production.put(100, 10000);
        production.put(200,19000);
        production.put(300, 27000);
        production.put(400, 35000);
        production.put(500, 41000);
        production.put(600, 50000);
        production.put(700, 54000);
    }

    public ProductionDto getProduction(int quantity) throws IllegalArgumentException {

        keys = production.keySet();
        maxValue = Collections.max(keys);

        if (quantity == 0) {
            return new ProductionDto(0, 0);
        }

        if (quantity < 0) {
            throw new IllegalArgumentException(quantity + " is less than 0!");
        }

        if (quantity >= maxValue) {
            return new ProductionDto(maxValue, production.get(maxValue));
        }

        if (production.containsKey(quantity)) {
            return new ProductionDto(quantity, production.get(quantity));
        }

        else {
            int temp = quantity + (100 - quantity % 100);
            return new ProductionDto(temp, production.get(temp));
        }
    }

    public int maxProduction() {
        return maxValue;
    }
}
