import { ChangeDetectorRef, Component, EventEmitter, Output } from '@angular/core';
import { NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { finalize } from 'rxjs';
import { AuthService } from '../auth/auth.service';
import { PortfolioApiService } from '../../services/portfolio-api.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, NgIf],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {
  @Output() loginSuccess = new EventEmitter<string>();

  username = '';
  submitted = false;
  isSubmitting = false;
  feedbackMessage = '';

  constructor(
    private authService: AuthService,
    private router: Router,
    private portfolioApiService: PortfolioApiService,
    private cdr: ChangeDetectorRef
  ) {}

  isValidEmail(value: string): boolean {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value.trim());
  }

  onSubmit(): void {
    this.submitted = true;
    this.feedbackMessage = '';

    if (!this.isValidEmail(this.username)) {
      this.feedbackMessage = 'Please enter a valid email address.';
      this.syncView();
      return;
    }

    this.isSubmitting = true;
    this.syncView();

    this.portfolioApiService.login(this.username.trim())
      .pipe(finalize(() => {
        this.isSubmitting = false;
        this.syncView();
      }))
      .subscribe({
        next: (portfolio) => {
          if (!portfolio) {
            this.feedbackMessage = 'No portfolio found for this email.';
            this.syncView();
            return;
          }

          console.log('Login successful, portfolio received:', portfolio);
          this.portfolioApiService.setCurrentPortfolio(portfolio);
          this.authService.login(this.username.trim());
          this.loginSuccess.emit(this.username.trim());
          this.router.navigate(['/dashboard']);
        },
        error: (error) => {
          this.feedbackMessage = error.message || 'Service unavailable. Please try again later.';
          this.syncView();
        },
      });
  }

  private syncView(): void {
    queueMicrotask(() => {
      try {
        this.cdr.detectChanges();
      } catch {
        // Ignore detectChanges errors after component teardown.
      }
    });
  }
}
