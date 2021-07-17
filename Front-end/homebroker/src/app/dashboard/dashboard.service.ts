import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import Stock from '../shared/model/stock-model';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  readonly baseUrl = "http://localhost:8080/api";

  constructor(private http: HttpClient) { }

  async findTodayStoks(): Promise<Stock[]> { 
    return this.http.get<Stock[]>(`${this.baseUrl}/stocks/today`).toPromise();
  }
}
