import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { provideNoopAnimations } from '@angular/platform-browser/animations';

import { ProfitLossCalculatorComponent } from './profit-loss-calculator.component';
import { ShipmentService } from '../../services/service';
import { ProfitLoss } from '../../models/calculation.model';

describe('ProfitLossCalculatorComponent', () => {
  let component: ProfitLossCalculatorComponent;
  let fixture: ComponentFixture<ProfitLossCalculatorComponent>;
  let serviceSpy: jasmine.SpyObj<ShipmentService>;

  const calculatedMock: ProfitLoss = {
    shipmentReference: 'A1', totalIncome: 230.5, totalCost: 200, profitLossValue: 30.5, calculatedOn: '2026-07-26T10:00:00Z',
  };

  beforeEach(async () => {
    serviceSpy = jasmine.createSpyObj('ShipmentService', ['getHistory', 'calculate']);
    serviceSpy.getHistory.and.returnValue(of([]));

    await TestBed.configureTestingModule({
      imports: [ProfitLossCalculatorComponent],
      providers: [
        provideNoopAnimations(),
        { provide: ShipmentService, useValue: serviceSpy },
        { provide: MatSnackBar, useValue: jasmine.createSpyObj('MatSnackBar', ['open']) },
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { paramMap: convertToParamMap({ shipmentReference: 'A1' }) } },
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ProfitLossCalculatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('with the correct request, should call the calculate() to update the results', () => {
    serviceSpy.calculate.and.returnValue(of(calculatedMock));
    component.form.setValue({ incomeValue: 230.5, costValue: 200, additionalCostValue: 0 });

    component.onCalculate();

    expect(serviceSpy.calculate).toHaveBeenCalledWith({
      shipmentReference: 'A1',
      incomeValue: 230.5,
      costs: { costValue: 200, additionalCostValue: 0 },
    });
    expect(component.results[0]).toEqual(calculatedMock);
    expect(component.loading).toBeFalse();
  });
});
