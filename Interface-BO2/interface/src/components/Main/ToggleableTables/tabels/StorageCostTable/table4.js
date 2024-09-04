import React, { useState } from "react";
import './table4.css';

const Table4 = ({ data, onDataChange }) => {
  const handleDataChange = (rowIndex, field, value) => {
    const newData = [...data];
    newData[rowIndex][field] = value;
    onDataChange(newData);
  };

  return (
    <table className="table">
      <thead>
        <tr>
          <th className="FirstRow">Ilość produktów</th>
          <th className="FirstRow">Koszt magazynowania</th>
        </tr>
      </thead>
      <tbody>
        {data.map((item, index) => (
          <tr className="RestTab" key={item.id}>
            <td className="RestTab">{item.id}</td>
            <td >
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

export default Table4;