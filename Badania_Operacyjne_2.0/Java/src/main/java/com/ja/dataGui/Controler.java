package com.ja.dataGui;

import com.ja.model.dto.HistoryDTO;
import com.ja.model.part.FactoryImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controler {

    FactoryImpl factory;

    public Controler() {
        factory = new FactoryImpl();
    }

    @CrossOrigin("http://localhost:4200/")
    @PostMapping("/test")
    public ResponseEntity<ResponseDTO> one(@RequestBody FactoryDetails temp) {
        System.out.println(temp);
        factory.setProductionCost(temp.getProductionCostMap());
        factory.setStorageCost(temp.getStorageCostMap());
        factory.setTruckPenalty(temp.getTruckCostMap());
        factory.setDonateValue(temp.getDonateValue());
        factory.setCumulativePenalty(temp.getCumulativePenaltyValue());
        factory.setExpectedProduction(temp.getExpectedProduction());
        factory.setSwarmSize(temp.getSwarmSize());
        factory.setInertia(temp.getInertia());
        factory.setC1(temp.getC1());
        factory.setC2(temp.getC2());
        factory.setIterStop(temp.getIterStop());

        HistoryDTO history = factory.compute();
        var costHistory = factory.getCostHistory();

        return ResponseEntity.ok(new ResponseDTO(history, costHistory));
    }
}
