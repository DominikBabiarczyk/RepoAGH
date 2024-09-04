package com.ja.model.part;

import java.util.HashMap;
import java.util.Map;

public class TruckPenalty implements DayIncrementable {

    private int currentDay;
    private Map<Integer, Double> penaltyMap;

    public TruckPenalty() {
        currentDay = -1;

        penaltyMap = new HashMap<>();
        penaltyMap.put(1, 2000.);
        penaltyMap.put(2, 5000.);
        penaltyMap.put(3, 4000.);
        penaltyMap.put(4, 3000.);
        penaltyMap.put(5, 5000.);
        penaltyMap.put(6, 2000.);
        penaltyMap.put(7, 2000.);
        penaltyMap.put(8, 2000.);
        penaltyMap.put(9, 3000.);
        penaltyMap.put(10, 2000.);
    }

    public TruckPenalty(Map<Integer, Double> truckPenaltyMap) {

        currentDay = -1;
        setTruckPenalty(truckPenaltyMap);
    }

    public void setTruckPenalty(Map<Integer, Double> truckPenaltyMap) {

        penaltyMap = new HashMap<>(truckPenaltyMap);
    }

    @Override
    public void incrementDay() {
        currentDay++;
    }

    public double getPenalty(int count) throws IllegalArgumentException {

        if(count < 0) {
            throw new IllegalArgumentException();
        }

        else {
            return penaltyMap.get(currentDay + 1) * count;
        }
    }
}
