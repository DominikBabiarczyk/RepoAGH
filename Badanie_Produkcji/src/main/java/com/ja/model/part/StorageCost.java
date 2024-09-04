package com.ja.model.part;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StorageCost {

    private Map<Integer, Integer> storageMap;
    private Set<Integer> keys;
    private int maxValue;

    public StorageCost() {

        storageMap = new HashMap<>();

        storageMap.put(100, 3000);
        storageMap.put(200, 4000);
        storageMap.put(300, 6000);
        storageMap.put(400, 8000);
        storageMap.put(500, 7000);
        storageMap.put(600, 8000);
        storageMap.put(700, 9000);

        keys = storageMap.keySet();
        maxValue = Collections.max(keys);
    }

    public int getCost(int quantity) throws IllegalArgumentException {

        if(quantity == 0) {
            return 0;
        }

        if(quantity < 0) {
            throw new IllegalArgumentException(quantity + " is not positive!");
        }

        if (quantity >= maxValue) {
            return storageMap.get(maxValue);
        }

        if (storageMap.containsKey(quantity)) {
            return storageMap.get(quantity);
        }

        else {
            int temp = quantity + (100 - quantity % 100);
            return storageMap.get(temp);
        }
    }
}
