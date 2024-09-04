import React from "react"
import './LeftPanel.css' 
import Title from './title';
import HerdSize from './OptionAlgorytm/HerdSize/HerdSize'
import Inertia from "./OptionAlgorytm/inertia/Inertia"
import CognitiveFactor from './OptionAlgorytm/CognitiveFactor/CognitiveFactor'
import SocialFactor from "./OptionAlgorytm/SocialFactor/SocialFactor";
import NumberIteration from "./OptionAlgorytm/NumberIteration/NumberIteration";

const LeftPanel = () =>{
  return(
    <div className="LeftPanel">
      <Title/>
      <HerdSize/>
      <Inertia/>
      <CognitiveFactor/>
      <SocialFactor/>
      <NumberIteration/>
    </div>
  )
}

export default LeftPanel;
