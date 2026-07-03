import { Routes } from '@angular/router';
import { AuthGuard } from './features/auth/auth.guard';
import { DashboardRouteComponent } from './features/dashboard/dashboard.route.component';
import { LoginRouteComponent } from './features/login/login.route.component';

export const routes: Routes = [
  { path: 'login', component: LoginRouteComponent },
  { path: 'dashboard', component: DashboardRouteComponent, canActivate: [AuthGuard] },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
];
