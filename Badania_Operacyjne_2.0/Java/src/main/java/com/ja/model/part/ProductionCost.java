package com.ja.model.part;

import com.ja.model.dto.ProductionDto;

import java.util.*;

public class ProductionCost {

    private Map<Integer, Double> production;
    private Set<Integer> keys;
    private int maxValue;
    private List<Integer> keysList;

    public ProductionCost() {

        Map<Integer, Double> temp = new HashMap<>();

        //liczba produktów - cena
        temp.put(100, 10000.);
        temp.put(200, 19000.);
        temp.put(300, 27000.);
        temp.put(400, 35000.);
        temp.put(500, 41000.);
        temp.put(600, 50000.);
        temp.put(700, 54000.);

        this.setProductionCostMap(temp);
    }

    public ProductionCost(Map<Integer, Double> productionCostMap) {

        this.setProductionCostMap(productionCostMap);
    }

    public void setProductionCostMap(Map<Integer, Double> productionCostMap) {

        production = new HashMap<>(productionCostMap);
        keys = production.keySet();
        keysList = new ArrayList<>(keys);
        Collections.sort(keysList);
        maxValue = Collections.max(keys);
    }

    public ProductionDto getProduction(int quantity) throws IllegalArgumentException {

        if (quantity == 0) {
            return new ProductionDto(0, 0);
        }

        if (quantity < 0) {
            throw new IllegalArgumentException(quantity + " is less than 0!");
        }

        if (quantity >= maxValue) {
            return new ProductionDto(maxValue, production.get(maxValue));
        }

//        if (production.containsKey(quantity)) {
//            return new ProductionDto(quantity, production.get(quantity));
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
                throw new RuntimeException("Production Cost error");
            }

//            int temp = quantity + (100 - quantity % 100);
            return new ProductionDto(currentQuantity, production.get(currentQuantity));
//            return new ProductionDto(temp, production.get(temp));
        }
    }

    public int maxProduction() {
        return maxValue;
    }
}
