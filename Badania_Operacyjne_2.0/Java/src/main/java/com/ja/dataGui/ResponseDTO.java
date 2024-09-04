package com.ja.dataGui;

import com.ja.model.dto.HistoryDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseDTO {
    private List<Integer> warehouseHistory;
    private List<Integer> productionHistoryRounded;
    private List<Integer> productionHistory;
    private double totalCost;
    private int dayMax;
    private List<Double> costHistory;

    public ResponseDTO(HistoryDTO historyDTO, List<Double> costHistory) {
        this.warehouseHistory = historyDTO.getWarehouseHistory();
        this.productionHistoryRounded = historyDTO.getProductionHistoryRounded();
        this.productionHistory = historyDTO.getProductionHistory();
        this.totalCost = historyDTO.getTotalCost();
        this.dayMax = historyDTO.getDayMax();

        this.costHistory = costHistory;
    }
}
