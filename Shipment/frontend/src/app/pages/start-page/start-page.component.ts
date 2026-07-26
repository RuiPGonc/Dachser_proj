import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MatListModule } from '@angular/material/list';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import {ShipmentService} from '../../services/service';
import {MatOption, MatSelect} from "@angular/material/select";

/**
 * First page of the flow: lets the user pick an existing shipment reference
 * from a closed dropdown (no free-text entry) and navigate to the
 * "Calculate Profit" page for that shipment.
 */
@Component({
  selector: 'app-start-page',
  standalone: true,
  imports: [CommonModule, FormsModule, MatListModule, MatButtonModule, MatFormFieldModule, MatInputModule, MatSelect, MatOption],
  templateUrl: './start-page.component.html',
  styleUrl: './start-page.component.scss'
})
export class StartPageComponent implements OnInit{

  private readonly service = inject(ShipmentService);
  private readonly router = inject(Router);

  shipmentReferences: string[] = [];
  selected = '';

  ngOnInit(): void {
    this.service.getAllShipmentReferences().subscribe({
      next: (list) => (this.shipmentReferences = list),
      error: (err) => console.error('Erro ao carregar shipments', err),
    });
  }

  /** Navigates to the calculation page for the currently selected shipment. */
  goToCalculate(): void {
    if (!this.selected) return;
    this.router.navigate(['/calculate', this.selected]);
  }
}
