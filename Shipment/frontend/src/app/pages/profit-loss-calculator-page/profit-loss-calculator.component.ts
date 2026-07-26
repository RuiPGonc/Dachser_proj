import {Component, inject, OnInit} from '@angular/core';
import {MatButtonModule} from "@angular/material/button";
import {MatInputModule} from "@angular/material/input";
import {MatFormFieldModule} from "@angular/material/form-field";
import {FormBuilder, ReactiveFormsModule, Validators} from "@angular/forms";
import {ProfitLoss, CalculationRequest} from "../../models/calculation.model";
import {ErrorResponse} from "../../models/error.model";
import {ShipmentService} from "../../services/service";
import {ProfitLossResultsComponent} from "../../components/profit-loss-results/profit-loss-results.component";
import {HttpErrorResponse} from '@angular/common/http';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import {ActivatedRoute} from '@angular/router';


/**
 * "Calculate Profit" page.
 * Loads the calculation history for the shipment coming from the route,
 * lets the user enter income/costs, and submits the calculation to the backend.
 * Implements the "Calculate Profit" use case described in the assessment document.
 */
@Component({
  selector: 'app-profit-loss-calculator',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    ProfitLossResultsComponent,
    MatSnackBarModule,
  ],
  templateUrl: './profit-loss-calculator.component.html',
  styleUrl: './profit-loss-calculator.component.scss',
})

export class ProfitLossCalculatorComponent {
  private readonly route = inject(ActivatedRoute);
  private readonly fb = inject(FormBuilder);
  private readonly service = inject(ShipmentService);
  private readonly snackBar = inject(MatSnackBar);
  results: ProfitLoss[] = [];
  loading = false;
  shipmentReference = '';

  /**
   * Flat form model for income/costs. It is translated into the nested
   * DTO expected by the backend on submit.
   */
  readonly form = this.fb.group({
    incomeValue: [0, [Validators.required, Validators.min(0)]],
    costValue: [0, [Validators.required, Validators.min(0)]],
    additionalCostValue: [0, [Validators.required, Validators.min(0)]],
  });

  ngOnInit(): void {
    this.shipmentReference = this.route.snapshot.paramMap.get('shipmentReference') ?? '';

    this.service.getHistory(this.shipmentReference).subscribe({
      next: (list) => (this.results = list),
      error: (err) => this.handleError(err),
    });
  }
  /**
   * Validates the form and, if valid, sends the calculation request to the backend.
   * On success, prepends the new result to the history and resets the form to zero.
   * On failure (or invalid form), shows an error message via the snackbar.
   */
  onCalculate(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.snackBar.open(
        'Check the fields: the values must be greater or equal to zero',
        'Close',
        {duration: 4000}
      );
      return;
    }

    const v = this.form.getRawValue();
    const request: CalculationRequest = {
      shipmentReference: this.shipmentReference!,
      incomeValue: Number(v.incomeValue),
      costs: {
        costValue: Number(v.costValue),
        additionalCostValue: Number(v.additionalCostValue),
      },
    };

    this.loading = true;

    this.service.calculate(request).subscribe({
      next: (data) => {
        this.results = [data, ...this.results];
        this.loading = false;
        this.form.reset({
          incomeValue: 0,
          costValue: 0,
          additionalCostValue: 0,
        });
      },
      error: (err) => this.handleError(err),
    });
  }

  /** Error handling — fulfils the "notifies in the UI" step of the use case. */
  private handleError(err: HttpErrorResponse): void {
    this.loading = false;
    const apiError = err.error as ErrorResponse;
    const message = apiError?.message ?? 'An unexpected error occurred.';
    this.snackBar.open(message, 'Close', {duration: 5000});
    console.error('Calculation failed', err);
  }
}
