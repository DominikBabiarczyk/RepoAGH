package com.ja.model.part;

public class Donate {

    private History history;
    private final int donateValue = 2000;

    public Donate(History history) {
        this.history = history;
    }

    public int getDonate() {
        if(history.getDayMax() - 1 == history.getDayCounter()) {
            int count = 0;

            for(int i = 0; i < history.getDayMax() - 1; i++) {
                if(history.getProduction(i) < history.getProduction(i + 1)) {
                    count++;
                }
            }

            return count * donateValue;
        }

        else {
            throw new RuntimeException("Donate: Wrong Day!");
        }
    }
}
