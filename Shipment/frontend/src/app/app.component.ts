import { Component, inject, OnInit } from '@angular/core';
import { Router, ActivatedRoute, NavigationEnd, RouterOutlet } from '@angular/router';
import { Location } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { filter } from 'rxjs';

/**
 * Root shell of the application.
 * Renders the header, the sidebar (current page name + "Back" button)
 * and the <router-outlet /> where the active page is displayed.
 */
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, MatIconModule, MatButtonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly location = inject(Location);

  title = 'Dachser app';
  pageTitle = '';
  showBackButton = false;

  ngOnInit(): void {
    this.updatePageTitle();

    // Re-evaluate the sidebar label / back-button every time the route changes.
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => this.updatePageTitle());
  }

  goBack(): void {
    this.location.back();
  }

  /**
   * Walks down to the deepest activated route (the one actually being rendered)
   * and reads its `data.pageTitle` / `data.showBack`, since route data isn't
   * automatically inherited by the root ActivatedRoute.
   */
  private updatePageTitle(): void {
    let current = this.route;
    while (current.firstChild) {
      current = current.firstChild;
    }
    this.pageTitle = current.snapshot.data['pageTitle'] ?? '';
    this.showBackButton = current.snapshot.data['showBack'] ?? false;
  }
}
