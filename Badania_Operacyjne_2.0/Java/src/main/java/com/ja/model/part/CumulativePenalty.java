package com.ja.model.part;


public class CumulativePenalty {

    private final TruckQueue truckQueue;

    //kara od każdego brakującego produktu
    public double penalty;

    public CumulativePenalty(TruckQueue queue) {
        this.truckQueue = queue;
        this.penalty = 97.5;
    }

    public CumulativePenalty(TruckQueue queue, double penalty) {
        this.truckQueue = queue;
        this.penalty = penalty;
    }

    public void setPenalty(double penalty) {
        if (penalty < 0) {
            throw new RuntimeException("Penalty can't be less than zero!");
        }

        else {
            this.penalty = penalty;
        }
    }

    public double getPenalty() {

        int missedProduction = 0;

        for(var i : truckQueue) {
            missedProduction += i;
        }

        if (missedProduction < 0) {
            throw new RuntimeException("missedProduction is lower than 0!");
        } else {
            return missedProduction * penalty;
        }
    }
}
