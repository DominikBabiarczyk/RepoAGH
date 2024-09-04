package com.ja.model.part;

import java.util.List;

public class CostFunction implements DayIncrementable, IFinishDay{

    private int dayMax;
    private int currentDay = -1;

    private History history;

    private ProductionCost productionCost;
    private StorageCost storageCost;

    private TruckPenalty truckPenalty;
    private final Storage storage;
    private final Production production;
    private final TruckQueue truckQueue;

    private final DayIncrementableComposite dayIncrementableComposite;
    private final FinishDayComposite finishDayComposite;

    private final Donate donate;

    public CostFunction(List<Integer> expectedProduction, List<Integer> scheduledProduction, int dayMax) throws IllegalArgumentException {

        if(!((expectedProduction.size() == scheduledProduction.size()) && (scheduledProduction.size() == dayMax))) {
            throw new IllegalArgumentException();
        }

        this.dayMax = dayMax;

        history = new History(dayMax);

        productionCost = new ProductionCost();
        storageCost = new StorageCost();

        truckPenalty = new TruckPenalty();
        storage = new Storage(history, storageCost);
        production = new Production(history, productionCost, scheduledProduction);
        truckQueue = new TruckQueue(history, truckPenalty, expectedProduction);

        dayIncrementableComposite = new DayIncrementableComposite(dayMax);
        dayIncrementableComposite.addDayIncrementable(truckPenalty, storage, production, truckQueue, history);

        finishDayComposite = new FinishDayComposite();
        finishDayComposite.addFinishDayComposite(truckQueue);
        finishDayComposite.addFinishDayComposite(storage);

        donate = new Donate(history);
    }

    @Override
    public void incrementDay() {
        currentDay++;
        dayIncrementableComposite.incrementDay();
    }

    @Override
    public void finishDay() {
        finishDayComposite.finishDay();
    }

    public double getCost() {
        boolean enoughProduction;

        while(currentDay < dayMax - 1) {
            this.incrementDay();

            enoughProduction = true;
            while(enoughProduction && !truckQueue.isEmpty()) {

                int requiredProduction = truckQueue.peek();

                int currentProduction = production.getCurrentProduction();

                if(currentProduction >= requiredProduction) {
                    truckQueue.removeTruck();
                    storage.storeProduction(currentProduction - requiredProduction);

                } else if (currentProduction + storage.getCurrentStorage() < requiredProduction) {
                    enoughProduction = false;

                } else if (currentProduction + storage.getCurrentStorage() >= requiredProduction) {
                    truckQueue.removeTruck();
                    storage.takeStoredProduction(requiredProduction - currentProduction);
                }
            }

            this.finishDayComposite.finishDay();
        }

        int cost = history.getTotalCost();
        int donateCost = donate.getDonate();
        cost -= donateCost;

        return cost;
    }

}
