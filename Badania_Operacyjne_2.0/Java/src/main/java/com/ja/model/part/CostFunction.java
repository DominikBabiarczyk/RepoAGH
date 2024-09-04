package com.ja.model.part;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@AllArgsConstructor
public class CostFunction implements DayIncrementable, IFinishDay{

    private final int dayMax;
    private int currentDay = -1;

    @Getter
    private final History history;

    private final ProductionCost productionCost;
    private final StorageCost storageCost;

    private final TruckPenalty truckPenalty;
    private final Storage storage;
    private final Production production;
    private final TruckQueue truckQueue;

    private final DayIncrementableComposite dayIncrementableComposite;
    private final FinishDayComposite finishDayComposite;

    private final Donate donate;
    private final CumulativePenalty cumulativePenalty;

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
        cumulativePenalty = new CumulativePenalty(truckQueue);
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
            int currentProduction = production.getCurrentProduction();

            while(enoughProduction) {

                // if there are no truck in the queue
                if(truckQueue.isEmpty()) {
                    enoughProduction = false;
                    storage.storeProduction(currentProduction);

                } else {
                    int requiredProduction = truckQueue.peek();

                    // if today's production is bigger than required production
                    if(currentProduction >= requiredProduction) {
                        truckQueue.removeTruck();
                        currentProduction -= requiredProduction;

                        // if today's production + production in storage is smaller than required production
                    } else if (currentProduction + storage.getCurrentStorage() < requiredProduction) {
                        enoughProduction = false;
                        storage.storeProduction(currentProduction);

                        // if today's production + production in storage is greater than required production
                    } else if (currentProduction + storage.getCurrentStorage() >= requiredProduction) {
                        truckQueue.removeTruck();
                        storage.takeStoredProduction(requiredProduction - currentProduction);
                        currentProduction = 0;

                    }
                }
            }

            this.finishDayComposite.finishDay();
        }

        double cost = history.getTotalCost();

        double donateCost = donate.getDonate();
        cost -= donateCost;

        double penaltyCost = cumulativePenalty.getPenalty();
        cost += penaltyCost;

        return cost;
    }

    public static class CostFunctionBuilder {

        private Map<Integer, Double> productionCostMap;
        private Map<Integer, Double> storageCostMap;
        private Map<Integer, Double> truckCostMap;
        private double donateValue;
        private double cumulativePenaltyValue;
        private int maxDay;
        private List<Integer> scheduledProduction;
        private List<Integer> expectedProduction;

        public CostFunctionBuilder() {
            reset();
        }

        public CostFunctionBuilder setProductionCostMap(Map<Integer, Double> costMap) {
            this.productionCostMap = costMap;
            return this;
        }

        public CostFunctionBuilder setStorageCostMap(Map<Integer, Double> storageCostMap) {
            this.storageCostMap = storageCostMap;
            return this;
        }

        public CostFunctionBuilder setTruckCostMap(Map<Integer, Double> truckCostMap) {
            this.truckCostMap = truckCostMap;
            return this;
        }

        public CostFunctionBuilder setDonateValue(double donateValue) {
            this.donateValue = donateValue;
            return this;
        }

        public CostFunctionBuilder setCumulativePenaltyValue(double penaltyValue) {
            this.cumulativePenaltyValue = penaltyValue;
            return this;
        }

        public CostFunctionBuilder setScheduledProduction(List<Integer> scheduledProduction) {
            this.scheduledProduction = scheduledProduction;
            return this;
        }

        public CostFunctionBuilder setExpectedProduction(List<Integer> expectedProduction) {
            this.expectedProduction = expectedProduction;
            return this;
        }

        public void reset() {
            productionCostMap = new HashMap<>();
            productionCostMap.put(100, 10000.);
            productionCostMap.put(300, 27000.);
            productionCostMap.put(200, 19000.);
            productionCostMap.put(400, 35000.);
            productionCostMap.put(500, 41000.);
            productionCostMap.put(600, 50000.);
            productionCostMap.put(700, 54000.);
            
            storageCostMap = new HashMap<>();
            storageCostMap.put(100, 3000.);
            storageCostMap.put(200, 4000.);
            storageCostMap.put(300, 6000.);
            storageCostMap.put(400, 8000.);
            storageCostMap.put(500, 7000.);
            storageCostMap.put(600, 8000.);
            storageCostMap.put(700, 9000.);

            truckCostMap = new HashMap<>();
            truckCostMap.put(1, 2000.);
            truckCostMap.put(2, 5000.);
            truckCostMap.put(3, 4000.);
            truckCostMap.put(4, 3000.);
            truckCostMap.put(5, 5000.);
            truckCostMap.put(6, 2000.);
            truckCostMap.put(7, 2000.);
            truckCostMap.put(8, 2000.);
            truckCostMap.put(9, 3000.);
            truckCostMap.put(10, 2000.);

            this.donateValue = 2000;
            this.cumulativePenaltyValue = 97.5;
            this.maxDay = 10;

            expectedProduction = Arrays.asList(
                    400,
                    450,
                    300,
                    1000,
                    100,
                    400,
                    450,
                    600,
                    600,
                    200
            );

            scheduledProduction = Arrays.asList(
                    0,
                    0,
                    724,
                    280,
                    61,
                    0,
                    0,
                    0,
                    0,
                    1000
            );
        }

        public CostFunction build() {
            if(expectedProduction.size() != scheduledProduction.size()) {
                throw new RuntimeException("maxDay can't be calculated properly!");
            }

            this.maxDay = expectedProduction.size();

            History history = new History(maxDay);

            ProductionCost productionCost = new ProductionCost(productionCostMap);
            StorageCost storageCost = new StorageCost(storageCostMap);

            TruckPenalty truckPenalty = new TruckPenalty(truckCostMap);
            Storage storage = new Storage(history, storageCost);
            Production production = new Production(history, productionCost, scheduledProduction);
            TruckQueue truckQueue = new TruckQueue(history, truckPenalty, expectedProduction);

            DayIncrementableComposite dayIncrementableComposite = new DayIncrementableComposite(maxDay);
            dayIncrementableComposite.addDayIncrementable(truckPenalty, storage, production, truckQueue, history);

            FinishDayComposite finishDayComposite = new FinishDayComposite();
            finishDayComposite.addFinishDayComposite(truckQueue);
            finishDayComposite.addFinishDayComposite(storage);

            Donate donate = new Donate(history, donateValue);
            CumulativePenalty cumulativePenalty = new CumulativePenalty(truckQueue, cumulativePenaltyValue);

            return new CostFunction(this.maxDay, -1, history, productionCost, storageCost, truckPenalty,
                    storage, production, truckQueue, dayIncrementableComposite, finishDayComposite, donate, cumulativePenalty);
        }

    }

    public static CostFunctionBuilder builder() {
        return new CostFunctionBuilder();
    }
}
