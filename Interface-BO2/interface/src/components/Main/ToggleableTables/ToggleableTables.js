// ToggleableTables.js
import React, { useState } from "react";
import Table1 from "./tabels/productionCostTable/table2";
import Table2 from "./tabels/StorageCostTable/table4";
import Table3 from "./tabels/Requirements/table3";
import Table4 from "./tabels/penaltyTable/Table1";
import './ToggleableTables.css';

const ToggleableTables = () => {
    const [selectedTable, setSelectedTable] = useState("production");

    const [productionCost, setProductionCost] = useState([
      { id: 100, cost: '' },
      { id: 200, cost: '' },
      { id: 300, cost: '' },
      { id: 400, cost: '' },
      { id: 500, cost: '' },
      { id: 600, cost: '' },
      { id: 700, cost: '' },
      { id: 800, cost: '' },
      { id: 900, cost: '' },
      { id: 1000, cost: ''}
    ]);
  
    const [storageCost, setStorageCost] = useState([
        { id: 100, cost: '' },
        { id: 200, cost: '' },
        { id: 300, cost: '' },
        { id: 400, cost: '' },
        { id: 500, cost: '' },
        { id: 600, cost: '' },
        { id: 700, cost: '' },
    ]);

    const [requirements, setRequirements] = useState([
        { id: 1, cost: '' },
        { id: 2, cost: '' },
        { id: 3, cost: '' },
        { id: 4, cost: '' },
        { id: 5, cost: '' },
        { id: 6, cost: '' },
        { id: 7, cost: '' },
        { id: 8, cost: '' },
        { id: 9, cost: '' },
        { id: 10, cost: '' }
    ]);

    const [penalty, setPenalty] = useState([
        { id: 1, cost: '' },
        { id: 2, cost: '' },
        { id: 3, cost: '' },
        { id: 4, cost: '' },
        { id: 5, cost: '' },
        { id: 6, cost: '' },
        { id: 7, cost: '' },
        { id: 8, cost: '' },
        { id: 9, cost: '' },
        { id: 10, cost: '' }
    ]);
  
    const handleToggle = (table) => {
      setSelectedTable(table);
    };
  
    const handleDataChange = (newData) => {
      if (selectedTable === 'production') {
        setProductionCost(newData);
      } else if (selectedTable === 'storage') {
        setStorageCost(newData);
      } else if (selectedTable === 'requirements') {
        setRequirements(newData);
      } else if (selectedTable === 'penalty') {
        setPenalty(newData);
      }
    };

  return (
    <div className="ToggleableTables">
      <div className="buttons">
        <button onClick={() => handleToggle("production")}>Koszt produkcji</button>
        <button onClick={() => handleToggle("storage")}>Koszt magazynowania</button>
        <button onClick={() => handleToggle("requirements")}>Wymagana produkcja</button>
        <button onClick={() => handleToggle("penalty")}>Kara za brak ciężarówki</button>
      </div>

      {selectedTable === "production" && (
        <Table1 data={productionCost} onDataChange={handleDataChange} />
      )}
      
      {selectedTable === "storage" &&   (
        <Table2 data={storageCost} onDataChange={handleDataChange} />
      )}

      {selectedTable === "requirements" && (
        <Table3 data={requirements} onDataChange={handleDataChange}/>
       )}

       {selectedTable === "penalty"  && (
        <Table4 data={penalty} onDataChange={handleDataChange}/>
       )}  
    </div>
  );
}

export default ToggleableTables;
