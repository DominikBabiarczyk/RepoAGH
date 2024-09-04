import React from "react";
import './HerdSize.css'

const herdSize = () =>{
    return(
      <div className="HerdSize">
        <p>Rozmiar stada (ilość cząstek)</p>
        <input type="text" placeholder="Wpisz wartość" className="input"/>
      </div>
    )
  }

export default herdSize;