package com.ja.model.part;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class History implements DayIncrementable {

    @Getter
    private int dayCounter;
    @Getter
    private List<Integer> warehouseHistory;
    @Getter
    private List<Integer> productionHistoryRounded;
    @Getter
    private List<Integer> productionHistory;
    @Getter
    private double totalCost;
    @Getter
    private int dayMax;

    public History(int dayMax) {
        this();
        this.dayMax = dayMax;
    }

    public History() {
        warehouseHistory = new ArrayList<>();
        productionHistoryRounded = new ArrayList<>();
        productionHistory = new ArrayList<>();

        dayCounter = -1;
        totalCost = 0;
    }

    @Override
    public void incrementDay() {
        dayCounter += 1;
    }

    public void addProductionRounded(int production) {
        productionHistoryRounded.add(production);
    }

    public void addProduction(int production) {
        this.productionHistory.add(production);
    }

    public void addWarehouse(int storage) {
        warehouseHistory.add(storage);
    }

    public void addCost(double cost) {
        totalCost += cost;
    }

    public int getProduction(int day) {
        return productionHistoryRounded.get(day);
    }

    private class ProductionHistoryIterator implements Iterator<Integer> {

        int index;
        List<Integer> productionHistory;

        ProductionHistoryIterator() {
            this.productionHistory = History.this.productionHistoryRounded;
            index = 0;
        }

        @Override
        public boolean hasNext() {
            return index < productionHistory.size();
        }

        @Override
        public Integer next() {
            if(hasNext()) {
                int temp = productionHistory.get(index);
                index++;
                return temp;
            }

            else {
                throw new NoSuchElementException();
            }
        }
    }

    public Iterator<Integer> getProductionHistoryIterator() {
        return new ProductionHistoryIterator();
    }

    @Override
    public String toString() {
        return "Production history: " + productionHistoryRounded.toString() + "\n" + "Warehouse history: " + warehouseHistory.toString();
    }
}
