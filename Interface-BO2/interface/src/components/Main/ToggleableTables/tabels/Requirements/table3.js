import React, { useState } from "react";
import './table3.css';

const Table3 = ({ data, onDataChange }) => {
  const handleDataChange = (rowIndex, field, value) => {
    const newData = [...data];
    newData[rowIndex][field] = value;
    onDataChange(newData);
  };

  return (
    <table className="table">
      <thead>
        <tr>
          <th className="FirstRow">Dzień</th>
          <th className="FirstRow">Wymagana ilość produktów</th>
        </tr>
      </thead>
      <tbody>
        {data.map((item, index) => (
          <tr className="RestTab" key={item.id}>
            <td className="RestTab" >{item.id}</td>
            <td>
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

export default Table3;