import {Component, Input, ViewChild, AfterViewInit, OnChanges, SimpleChanges} from '@angular/core';
import {CommonModule} from '@angular/common';
import {MatTableModule, MatTableDataSource} from '@angular/material/table';
import {ProfitLoss} from "../../models/calculation.model";
import {MatSortModule, MatSort} from '@angular/material/sort';

/**
 * Presentational ("dumb") component: receives data via @Input and only
 * renders it in a sortable table. Knows nothing about services or HTTP.
 */
@Component({
  selector: 'app-profit-loss-results',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatSortModule],
  templateUrl: './profit-loss-results.component.html',
  styleUrl: './profit-loss-results.component.scss'
})
export class ProfitLossResultsComponent implements OnChanges, AfterViewInit {
  @Input() data: ProfitLoss[] = [];
  @Input() shipmentReference = '';

  @ViewChild(MatSort) sort!: MatSort;

  readonly displayedColumns = [
    'totalIncome',
    'totalCost',
    'profitLossValue'
  ];
  readonly dataSource = new MatTableDataSource<ProfitLoss>([]);

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['data'] && this.sort) {

      // Reset sorting back to insertion order whenever new data arrives
      // (e.g. right after a new "Calculate").
      this.sort.active = '';
      this.sort.direction = '';
    }
    if (changes['data'] || changes['shipmentReference']) {
      // Extra safety net on top of the backend response: only display rows
      // that actually belong to the shipment currently open.
      this.dataSource.data = this.data.filter(
        row => row.shipmentReference === this.shipmentReference
      );
    }
  }

  ngAfterViewInit(): void {
    // Default sort when the table is first opened: most recent calculation first.
    this.sort.active = 'calculatedOn';
    this.sort.direction = 'desc';
    this.dataSource.sort = this.sort;
  }

  get isEmpty(): boolean {
    return this.dataSource.data.length === 0;
  }
}
