package com.ja.model.part;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String... args) {

        int numberOfDays = 10;

        List<Integer> expectedProduction = new ArrayList<>(Arrays.asList(
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
        ));

        List<Integer> scheduledProduction = new ArrayList<>(Arrays.asList(
                300,
                200,
                500,
                700,
                900,
                1000,
                100,
                100,
                200,
                300
        ));

        CostFunction function = new CostFunction(expectedProduction, scheduledProduction, numberOfDays);

        double cost = function.getCost();
        System.out.println("Cost is equal to: " + cost);
    }
}
