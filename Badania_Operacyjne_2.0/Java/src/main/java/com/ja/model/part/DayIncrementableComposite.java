package com.ja.model.part;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DayIncrementableComposite implements DayIncrementable{

    private int currentDay;
    private int maxDay;
    private List<DayIncrementable> dayIncrementableList;

    public DayIncrementableComposite(int maxDay) {
        dayIncrementableList = new ArrayList<>();
        this.maxDay = maxDay;
        currentDay = -1;
    }

    public void addDayIncrementable(DayIncrementable incrementable) {
        dayIncrementableList.add(incrementable);
    }

    public void addDayIncrementable(DayIncrementable... incrementables) {
        Arrays.stream(incrementables)
                .forEach(this::addDayIncrementable);
    }

    @Override
    public void incrementDay() {
        if(currentDay < maxDay) {
            currentDay++;
            for (var i : dayIncrementableList) {
                i.incrementDay();
            }
        }
    }
}
