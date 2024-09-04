package com.ja.model.part;

import com.ja.model.dto.ProductionDto;
import lombok.Getter;

import java.util.List;

public class Production implements DayIncrementable {

    private int currentDay;
    @Getter
    private int currentProduction;
    private List<Integer> scheduledProduction;
    private History history;
    private ProductionCost productionCost;

    public Production(History history, ProductionCost productionCost, List<Integer> scheduledProduction) {

        currentDay = -1;
        currentProduction = 0;
        this.scheduledProduction = scheduledProduction;
        this.history = history;
        this.productionCost = productionCost;
    }

    public Production(History history, ProductionCost productionCost) {

        currentDay = -1;
        currentProduction = 0;
        this.history = history;
        this.productionCost = productionCost;
    }

    @Override
    public void incrementDay() {
        currentDay++;
        int scheduledProductionTemp = this.scheduledProduction.get(currentDay);

        ProductionDto temp = productionCost.getProduction(scheduledProductionTemp);
        history.addProductionRounded(temp.getQuantity());
        history.addProduction(scheduledProductionTemp);

        history.addCost(temp.getValue());
        currentProduction = temp.getQuantity();
    }
}
