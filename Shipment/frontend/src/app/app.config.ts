import {ApplicationConfig} from '@angular/core';
import {provideRouter} from '@angular/router';
import {routes} from './app.routes';
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async';
import { provideHttpClient } from '@angular/common/http';

/**
 * Root application providers, wired up in main.ts via bootstrapApplication().
 * - provideRouter: enables the routes defined in app.routes.ts.
 * - provideAnimationsAsync: required by Angular Material components
 *   (mat-sort-header, mat-form-field, mat-snack-bar, ...) that rely on animations.
 * - provideHttpClient: enables HttpClient injection (used by ShipmentService).
 */
export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideAnimationsAsync(),
    provideHttpClient(),
]
};
