import { Component, OnInit } from '@angular/core';
import Stock from 'src/app/shared/model/stock-model';
import { DashboardService } from '../dashboard.service';

@Component({
  selector: 'app-dashboard-page',
  templateUrl: './dashboard-page.component.html',
  styleUrls: ['./dashboard-page.component.css']
})
export class DashboardPageComponent implements OnInit {

  stocks: Stock[] = [];

  constructor(private dashboardService: DashboardService) { }

  ngOnInit(): void {
    this.feachStocks();
  }

  async feachStocks(): Promise<void> {
    this.stocks = await this.dashboardService.findTodayStoks();
    console.log(this.stocks);
  }

}
