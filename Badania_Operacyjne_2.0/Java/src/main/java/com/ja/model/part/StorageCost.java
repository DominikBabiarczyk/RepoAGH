package com.ja.model.part;

import java.util.*;

public class StorageCost {

    private Map<Integer, Double> storageMap;
    private Set<Integer> keys;
    private int maxValue;
    private List<Integer> keysList;

    public StorageCost() {

        Map<Integer, Double> temp = new HashMap<>();

        //ilość w magazynie, kosz magazynowania
        temp.put(100, 3000.);
        temp.put(200, 4000.);
        temp.put(300, 6000.);
        temp.put(400, 8000.);
        temp.put(500, 7000.);
        temp.put(600, 8000.);
        temp.put(700, 9000.);

        this.setStorageCostMap(temp);
    }

    public StorageCost(Map<Integer, Double> storageCostMap) {

        setStorageCostMap(storageCostMap);
    }

    private void setStorageCostMap(Map<Integer, Double> storageCostMap) {

        storageMap = new HashMap<>(storageCostMap);
        keys = storageMap.keySet();
        keysList = new ArrayList<>(keys);
        Collections.sort(keysList);
        maxValue = Collections.max(keys);
    }

    public double getCost(int quantity) throws IllegalArgumentException {

        if(quantity == 0) {
            return 0;
        }

        if(quantity < 0) {
            throw new IllegalArgumentException(quantity + " is not positive!");
        }

        if (quantity >= maxValue) {
            return storageMap.get(maxValue);
        }

//        if (storageMap.containsKey(quantity)) {
//            return storageMap.get(quantity);
//        }

        else {
            int currentQuantity = 0;

            for(int i : keysList) {
                if(i >= quantity) {
                    currentQuantity = i;
                    break;
                }
            }

            if(currentQuantity == 0) {
                throw new RuntimeException("Storage Cost error");
            }

            return storageMap.get(currentQuantity);

//            int temp = quantity + (100 - quantity % 100);
//            return storageMap.get(temp);
        }
    }
}
