package com.ja.model.abstraction;

import com.ja.model.dto.HistoryDTO;
import com.ja.model.part.History;

import java.util.List;
import java.util.Map;

public interface IFactory {

    void setProductionCost(Map<Integer, Double> productionCostMap);

    void setStorageCost(Map<Integer, Double> storageCostMap);

    void setTruckPenalty(Map<Integer, Double> truckCostMap);

    void setDonateValue(double donateValue);

    void setCumulativePenalty(double cumulativePenaltyValue);

//    void setScheduledProduction(List<Integer> scheduledProduction);

    void setExpectedProduction(List<Integer> expectedProduction);

    HistoryDTO compute();

    void setSwarmSize(int swarmSize);

    void setInertia(double inertia);

    void setC1(double c1);

    void setC2(double c2);

    void setIterStop(int iterStop);
}
