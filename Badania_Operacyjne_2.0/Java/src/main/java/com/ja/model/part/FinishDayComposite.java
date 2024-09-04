package com.ja.model.part;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class FinishDayComposite implements IFinishDay {

    private List<IFinishDay> FinishDayList = new ArrayList<>();

    @Override
    public void finishDay() {
        for(var i : FinishDayList) {
            i.finishDay();
        }
    }

    public void addFinishDayComposite(IFinishDay finishDay) {
        FinishDayList.add(finishDay);
    }

    public void addFinishDayComposite(IFinishDay... finishDays) {
        Arrays.stream(finishDays)
                .forEach(this::addFinishDayComposite);
    }
}
