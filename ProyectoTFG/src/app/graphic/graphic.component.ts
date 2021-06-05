import { Component, OnInit, Input } from '@angular/core';
import { ChartOptions, ChartType } from 'chart.js';
import { Chart, registerables } from 'chart.js';
import { Label, SingleDataSet } from 'ng2-charts';

@Component({
  selector: 'app-graphic',
  templateUrl: './graphic.component.html',
  styleUrls: ['./graphic.component.css']
})
export class GraphicComponent implements OnInit {
  @Input() resultParam: [];
  @Input() param: [];
  public pieChartOptions: ChartOptions = {
    responsive: true,
  };
  public pieChartLabels: Label[] = ['5','10','15'];
  public pieChartData: SingleDataSet = [0.9919, 1.0495, 1.0368];
  public pieChartType: ChartType = 'line';
  public pieChartLegend = true;
  public pieChartPlugins = [];
  

  constructor() {
    Chart.register(...registerables);
   }

  ngOnInit() {  }

}
