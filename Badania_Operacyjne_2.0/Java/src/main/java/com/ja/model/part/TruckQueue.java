package com.ja.model.part;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TruckQueue implements DayIncrementable, IFinishDay, Iterable<Integer> {

    private int currentDay;
    private History history;

    private Queue<Truck> queue = new LinkedList<>();
    private List<Integer> expectedProductionList;
    private TruckPenalty truckPenalty;

    TruckQueue(History history, TruckPenalty truckPenalty, List<Integer> expectedProductionList) {

        currentDay = -1;
        this.history = history;
        this.truckPenalty = truckPenalty;
        this.expectedProductionList = expectedProductionList;
    }

    TruckQueue(History history, TruckPenalty truckPenalty) {

        currentDay = -1;
        this.history = history;
        this.truckPenalty = truckPenalty;
    }

    @Override
    public void incrementDay() {
        this.currentDay++;
        int expectedProduction = expectedProductionList.get(currentDay);
        queue.add(new Truck(expectedProduction));
    }

    public int peek() {
        return queue.peek().getExpectedProduction();
    }

    public void removeTruck() {
        queue.poll();
    }

    @Override
    public void finishDay() {
        queue.forEach(Truck::addDelay);

        int count = 0;
        for(var i : queue) {
            if(i.getDelay()  > 0) {
                count++;
            }
        }

        double cost = truckPenalty.getPenalty(count);
        history.addCost(cost);
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    private class TruckQueueIterator implements Iterator<Integer> {

        private final Iterator<Truck> linkedListIterator;

        public TruckQueueIterator() {
            linkedListIterator = queue.iterator();
        }

        @Override
        public boolean hasNext() {
            return linkedListIterator.hasNext();
        }

        @Override
        public Integer next() {
            return linkedListIterator.next().getExpectedProduction();
        }
    }

    @Override
    public Iterator<Integer> iterator() {
        return new TruckQueueIterator();
    }

    //    public static void main(String... args) {
//        LinkedList<Truck> aha = new LinkedList<>();
//        aha.add(new Truck(100));
//        aha.add(new Truck(200));
//        aha.add(new Truck(1000));
//
//        for(var a : aha) {
//            a.addDelay();
//        }
//
//        aha.forEach(Truck::addDelay);
//
//        for(var a : aha) {
//            System.out.println(a.getDelay());
//        }
//    }
}
