package com.ja.dataGui;

import lombok.*;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class FactoryDetails {

    private Map<Integer, Double> productionCostMap;
    private Map<Integer, Double> storageCostMap;

    private Map<Integer, Double> truckCostMap;

//    private List<Double> truckCostMap;

    private double donateValue;
    private double cumulativePenaltyValue;

    private List<Integer> expectedProduction;

    private int swarmSize;
    private double inertia;
    private double c1;
    private double c2;
    private int iterStop;
}
