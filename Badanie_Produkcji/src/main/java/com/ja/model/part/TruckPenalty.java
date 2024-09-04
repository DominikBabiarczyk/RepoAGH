package com.ja.model.part;

import java.util.HashMap;
import java.util.Map;

public class TruckPenalty implements DayIncrementable {

    private int currentDay;
    private Map<Integer, Integer> penaltyMap;

    public TruckPenalty() {
        currentDay = -1;

        penaltyMap = new HashMap<>();
        penaltyMap.put(1, 2000);
        penaltyMap.put(2, 5000);
        penaltyMap.put(3, 4000);
        penaltyMap.put(4, 3000);
        penaltyMap.put(5, 5000);
        penaltyMap.put(6, 2000);
        penaltyMap.put(7, 2000);
        penaltyMap.put(8, 2000);
        penaltyMap.put(9, 3000);
        penaltyMap.put(10, 2000);
    }

    @Override
    public void incrementDay() {
        currentDay++;
    }

    public int getPenalty(int count) throws IllegalArgumentException {

        if(count < 0) {
            throw new IllegalArgumentException();
        }

        else {
            return penaltyMap.get(currentDay + 1) * count;
        }
    }
}
