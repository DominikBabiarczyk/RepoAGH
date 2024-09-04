import { Component, OnInit } from '@angular/core';
import { PlotlyService } from '../plotly.service';

@Component({
  selector: 'app-graph',
  templateUrl: './graph.component.html',
  styleUrls: ['./graph.component.css']
})
export class GraphComponent implements OnInit{

  constructor(private plot: PlotlyService) { }

  ngOnInit(): void {
    let x:number[] = [1,2,3,4,5];
    let y:number[] = [1,2,3,4,5];
    this.plot.plotLine("Line Plot","plot",x,y);
  }
}
