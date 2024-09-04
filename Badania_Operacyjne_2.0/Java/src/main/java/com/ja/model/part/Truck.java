package com.ja.model.part;

import lombok.Getter;

@Getter
public class Truck {

    private int expectedProduction;
    private int delay;

    public Truck(int expectedProduction) {
        this.expectedProduction = expectedProduction;
        this.delay = 0;
    }

    public void addDelay() {
        delay++;
    }
}
