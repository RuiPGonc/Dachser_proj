import { Routes } from '@angular/router';
import { StartPageComponent } from './pages/start-page/start-page.component';
import { ProfitLossCalculatorComponent } from './pages/profit-loss-calculator-page/profit-loss-calculator.component';

/**
 * Application routes.
 * Each route carries a `data.pageTitle` (shown in the sidebar by AppComponent)
 * and a `data.showBack` flag (controls whether the "Back" button is displayed).
 */
export const routes: Routes = [
  { path: '', redirectTo: 'shipments', pathMatch: 'full' },
  { path: 'shipments', component: StartPageComponent, data: { pageTitle: 'Shipment Select', showBack: false  }},
  { path: 'calculate/:shipmentReference', component: ProfitLossCalculatorComponent,  data: { pageTitle: 'Calculate Profit' , showBack: true}},

];
