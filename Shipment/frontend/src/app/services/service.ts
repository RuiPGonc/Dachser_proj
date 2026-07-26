import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import { Observable } from 'rxjs';
import { CalculationRequest, ProfitLoss } from '../models/calculation.model';

/**
 * Handles all HTTP communication with the Profit/Loss backend API.
 * Kept free of UI concerns (no snackbars, no navigation) so it can be
 * reused and tested independently of any component.
 */
@Injectable({ providedIn: 'root' })
export class ShipmentService {
  private readonly baseUrl = '/api/profitLoss';

  constructor( private http: HttpClient) {}

  /** Submits income/cost data for a shipment and returns the calculated profit or loss. */
  calculate(request: CalculationRequest): Observable<ProfitLoss> {
    return this.http.post<ProfitLoss>(`${this.baseUrl}/calculation`, request);
  }

  /** Retrieves the calculation history for a given shipment reference. */
  getHistory(shipmentReference: string): Observable<ProfitLoss[]> {
    const params = new HttpParams().set('shipmentReference', shipmentReference);
    return this.http.get<ProfitLoss[]>(this.baseUrl, { params });
  }

  /** Retrieves all shipment references available for selection on the start page. */
  getAllShipmentReferences(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/references`);
  }
}
