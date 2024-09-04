package com.ja.optimgui.pso;

import com.ja.model.dto.HistoryDTO;
import com.ja.model.part.CostFunction;
import com.ja.optimgui.math.MVector;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Setter
@Getter
public class SolverBuilder {

    private int swarmSize;
    private double inertia;
    private double c1;
    private double c2;
    private int iterStop;
    private int numberOfDays;
    private CostFunction.CostFunctionBuilder costFunctionBuilder;
    private double lowerBoundaryValue;
    private double upperBoundaryValue;

    private CostFunction costFunction;

    public SolverBuilder() {
        swarmSize = 100;
        inertia = 0.8;
        c1 = 0.1;
        c2 = 0.1;
        iterStop = 10;
        numberOfDays = 10;
        lowerBoundaryValue = 0;
        upperBoundaryValue = 1000;
    }

    private double productionObjFunc(MVector scheduledProduction) {

        List<Double> temp = scheduledProduction.toList();
        List<Integer> scheduledProductionList = new ArrayList<>();

        temp.forEach((var i) -> scheduledProductionList.add(i.intValue()));
        costFunction = costFunctionBuilder.setScheduledProduction(scheduledProductionList).build();
        return costFunction.getCost();

    }

    public Solver build() throws Exception {

        if (costFunctionBuilder == null) {
            throw new Exception("Cost function is missing!");
        }

        double[] lowerBoundaryArray = new double[numberOfDays];
        double[] upperBoundaryArray = new double[numberOfDays];

        Arrays.fill(lowerBoundaryArray, lowerBoundaryValue);
        Arrays.fill(upperBoundaryArray, upperBoundaryValue);

        MVector lowerBoundary = new MVector(lowerBoundaryArray);
        MVector upperBoundary = new MVector(upperBoundaryArray);

        return new Solver(swarmSize, this::productionObjFunc, lowerBoundary, upperBoundary, iterStop, inertia, c1, c2);
    }

    public HistoryDTO getHistory() {

        return new HistoryDTO(costFunction.getHistory());
    }
}