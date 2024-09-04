import React from "react";
import './NumberIteration.css';

const NumberIteration = () => {
    return(
        <div className="NumberIteration">
            <p>Ilość iteracji z tą samą wartością do stopu</p>
            <input type="text" placeholder="Wpisz wartość" className="input" />
        </div>
    )
}

export default NumberIteration; 