package com.ja.model.part;

import com.ja.model.abstraction.IFactory;
import com.ja.model.dto.HistoryDTO;
import com.ja.optimgui.pso.Solver;
import com.ja.optimgui.pso.SolverBuilder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

public class FactoryImpl implements IFactory {

    private CostFunction.CostFunctionBuilder costFunctionBuilder;
    private SolverBuilder solverBuilder;
    @Getter
    private HistoryDTO historyDTO;
    @Getter
    private List<Double> costHistory;

    public FactoryImpl() {
        costFunctionBuilder = new CostFunction.CostFunctionBuilder();
        solverBuilder = new SolverBuilder();
    }

    @Override
    public void setProductionCost(Map<Integer, Double> productionCostMap) {
        costFunctionBuilder.setProductionCostMap(productionCostMap);
    }

    @Override
    public void setStorageCost(Map<Integer, Double> storageCostMap) {
        costFunctionBuilder.setStorageCostMap(storageCostMap);
    }

    @Override
    public void setTruckPenalty(Map<Integer, Double> truckCostMap) {
        costFunctionBuilder.setTruckCostMap(truckCostMap);
    }

    @Override
    public void setDonateValue(double donateValue) {
        costFunctionBuilder.setDonateValue(donateValue);
    }

    @Override
    public void setCumulativePenalty(double cumulativePenaltyValue) {
        costFunctionBuilder.setCumulativePenaltyValue(cumulativePenaltyValue);
    }

//    @Override
//    public void setScheduledProduction(List<Integer> scheduledProduction) {
//        costFunctionBuilder.setScheduledProduction()
//    }

    @Override
    public void setExpectedProduction(List<Integer> expectedProduction) {
        costFunctionBuilder.setExpectedProduction(expectedProduction);
    }

    @Override
    public HistoryDTO compute() {
        solverBuilder.setCostFunctionBuilder(costFunctionBuilder);

        try {
            Solver solver = solverBuilder.build();
            solver.solve();
            this.historyDTO = solverBuilder.getHistory();
            this.costHistory = solver.getCostHistory();
            return historyDTO;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setSwarmSize(int swarmSize) {
        solverBuilder.setSwarmSize(swarmSize);
    }

    @Override
    public void setInertia(double inertia) {
        solverBuilder.setInertia(inertia);
    }

    @Override
    public void setC1(double c1) {
        solverBuilder.setC1(c1);
    }

    @Override
    public void setC2(double c2) {
        solverBuilder.setC2(c2);
    }

    @Override
    public void setIterStop(int iterStop) {
        solverBuilder.setIterStop(iterStop);
    }
}
