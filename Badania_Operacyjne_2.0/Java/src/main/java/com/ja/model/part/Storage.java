package com.ja.model.part;

import lombok.Getter;

public class Storage implements DayIncrementable, IFinishDay {

    private int currentDay;
    @Getter
    private int currentStorage;
    private static final int maxStorage = 700;
    private StorageCost storageCost;
    private History history;

    public Storage(History history, StorageCost storageCost) {
        currentDay = -1;
        currentStorage = 0;
        this.storageCost = storageCost;
        this.history = history;
    }

    @Override
    public void incrementDay() {
        currentDay++;
    }

    @Override
    public void finishDay() {
        var cost = storageCost.getCost(currentStorage);
        history.addCost(cost);
        history.addWarehouse(currentStorage);
    }

    public int getRemainingStorage() {
        return maxStorage - currentStorage;
    }

    public boolean canStore(int storage) {
        return currentStorage + storage <= maxStorage;
    }

    public int takeStoredProduction(int production) throws IllegalArgumentException{
        if(production <= currentStorage) {
            currentStorage -= production;
            return production;
        }

        else {
            throw new IllegalArgumentException();
        }
    }

    public void storeProduction(int production) throws IllegalArgumentException {
        if(currentStorage + production <= maxStorage) {
            currentStorage += production;
        }

        else {
            currentStorage = maxStorage;
        }
    }
}
