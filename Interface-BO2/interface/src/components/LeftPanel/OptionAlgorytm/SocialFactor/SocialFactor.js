import React from "react";
import './SocialFactor.css';

const socialFactor = () => {
    return(
        <div className="SocialFactor">
            <p>Współczynnik społeczny (jak dużą wagę cząstka przywiązuje do najlepszej pozycji znalezionej przez stado)</p>
            <input type="text" placeholder="Wpisz wartość" className="input" />
        </div>
    )
}

export default socialFactor; 