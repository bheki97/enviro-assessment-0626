import { Component } from '@angular/core';
import { LoginComponent } from './login.component';

@Component({
  selector: 'app-login-route',
  standalone: true,
  imports: [LoginComponent],
  template: '<app-login></app-login>',
})
export class LoginRouteComponent {}
