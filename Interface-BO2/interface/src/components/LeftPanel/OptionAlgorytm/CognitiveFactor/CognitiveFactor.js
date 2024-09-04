import React from "react";
import './CognitiveFactor.css';

const cognitiveFactor = () => {
    return(
        <div className="CognitiveFactor">
            <p >Współczynnik poznawczy (jak dużą wagę cząstka przywiązuje do swojej pozycji)</p>
            <input type="text" placeholder="Wpisz wartość" className="input" />
        </div>
    );
}

export default cognitiveFactor; 