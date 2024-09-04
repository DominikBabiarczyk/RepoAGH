import logo from './logo.svg';
import './App.css';
import Header from './components/Header/Header'; // Dodaj ten import
import LeftPanel from './components/LeftPanel/LeftPanel';
import ToggleableTables from './components/Main/ToggleableTables/ToggleableTables';
import React, { useState, useEffect } from 'react';

function App() {

  const [selectedTable, setSelectedTable] = useState("data");
  const [data, setData] = useState([]);
  const [name, setName] = useState("ja");

  const handleToggle = (table) => {
    setSelectedTable(table);
  };

  useEffect(() => {
    fetch('http://localhost:8080/api/cassette/all')
      .then(response => response.json())
      .then(data => {
        console.log(data);
        setData(data);
      });
  }, []);

  return (

    <div className="App">
    <Header/>
      <div className='MainView'>
        <LeftPanel/>
        <div>
          <ToggleableTables selectedTable={selectedTable} onToggle={handleToggle}/>
        </div>
      </div>
    </div>
  );
}

export default App;
