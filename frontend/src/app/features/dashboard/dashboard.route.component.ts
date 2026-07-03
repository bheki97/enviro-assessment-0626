import { Component } from '@angular/core';
import { DashboardComponent } from './dashboard.component';

@Component({
  selector: 'app-dashboard-route',
  standalone: true,
  imports: [DashboardComponent],
  template: '<app-dashboard></app-dashboard>',
})
export class DashboardRouteComponent {}
