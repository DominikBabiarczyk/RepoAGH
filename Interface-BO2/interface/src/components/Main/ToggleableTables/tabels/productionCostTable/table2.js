import React, { useState } from "react";
import './table2.css';

const Table2 = ({ data, onDataChange }) => {
  const handleDataChange = (rowIndex, field, value) => {
    const newData = [...data];
    newData[rowIndex][field] = value;
    onDataChange(newData);
  };

  return (
    <table className="table">
      <thead>
        <tr>
          <th className="FirstRow">Ilość Produkcji</th>
          <th className="FirstRow">Koszt Produkcji</th>
        </tr>
      </thead>
      <tbody>
        {data.map((item, index) => (
          <tr key={item.id}>
            <td className="RestTab">{item.id}</td>
            <td className="RestTab">
              <input
                type="text"
                value={item.age}
                onChange={(e) => handleDataChange(index, 'age', e.target.value)}
              />
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
};

export default Table2;