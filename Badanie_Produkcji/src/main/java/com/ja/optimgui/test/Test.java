package com.ja.optimgui.test;

import com.ja.optimgui.math.MVector;
import com.ja.optimgui.pso.Solver;

import com.ja.model.part.CostFunction;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {

    //funkcja celu, w tym przypadku dwa argumenty
    public static double objFunc(MVector args){
        double x = args.getElement(0);
        double y = args.getElement(1);

        return Math.sqrt(Math.pow(x - 3, 2)+ Math.pow(y - 1, 2));
    }

    public static double productionObjFunc(MVector scheduledProduction) {
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

        List<Double> temp = scheduledProduction.toList();
        List<Integer> scheduledProductionList = new ArrayList<>();

        temp.forEach((var i) -> scheduledProductionList.add(i.intValue()));

        CostFunction function = new CostFunction(expectedProduction, scheduledProductionList, numberOfDays);
        return function.getCost();
    }

    public static void main(String[] args) {

        //Przedziały do rozpatrywania
        MVector lowBound = new MVector(new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        MVector upBound = new MVector(new double[]{1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000});

        int counter = 10;
        double w = 0.8;
        double c1 = 0.2;
        double c2 = 0.2;
        int swarmSize = 100000;

        Solver solver = new Solver(swarmSize, Test::productionObjFunc, lowBound, upBound, counter, w, c1, c2);

        solver.solve();
    }
}
