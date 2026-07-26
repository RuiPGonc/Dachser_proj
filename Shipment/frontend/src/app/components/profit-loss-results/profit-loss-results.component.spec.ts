import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideNoopAnimations } from '@angular/platform-browser/animations';
import { ProfitLoss } from '../../models/calculation.model';

import { ProfitLossResultsComponent } from './profit-loss-results.component';

describe('ProfitLossResultsComponent', () => {
  let fixture: ComponentFixture<ProfitLossResultsComponent>;
  let component: ProfitLossResultsComponent;

  const mockData: ProfitLoss[] = [
    { shipmentReference: 'A1', totalIncome: 1000, totalCost: 200, profitLossValue: 800, calculatedOn: '2026-07-20T10:00:00Z' },
    { shipmentReference: 'A1', totalIncome: 500, totalCost: 900, profitLossValue: -400, calculatedOn: '2026-07-21T10:00:00Z' },
    { shipmentReference: 'B2', totalIncome: 300, totalCost: 100, profitLossValue: 200, calculatedOn: '2026-07-22T10:00:00Z' },
  ];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProfitLossResultsComponent],
      providers: [provideNoopAnimations()],
    });
    fixture = TestBed.createComponent(ProfitLossResultsComponent);
    component = fixture.componentInstance;
  });

  it('Only shows the lines related to shipment reference A1', () => {
    fixture.componentRef.setInput('data', mockData);
    fixture.componentRef.setInput('shipmentReference', 'A1');
    fixture.detectChanges();

    expect(component.dataSource.data.length).toBe(2);
    expect(component.dataSource.data.every(row => row.shipmentReference === 'A1')).toBeTrue();
  });

  it('isEmpty should be true if there are no correspondence', () => {
    fixture.componentRef.setInput('data', mockData);
    fixture.componentRef.setInput('shipmentReference', 'NONE');
    fixture.detectChanges();

    expect(component.isEmpty).toBeTrue();
  });
});
