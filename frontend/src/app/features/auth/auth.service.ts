import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly authEmailKey = 'enviro.auth.email';
  private loggedIn = false;

  login(email: string): void {
    const normalizedEmail = email.trim().toLowerCase();
    this.loggedIn = normalizedEmail.length > 0;

    const storage = this.getSessionStorage();
    if (!storage) {
      return;
    }

    if (this.loggedIn) {
      storage.setItem(this.authEmailKey, normalizedEmail);
      return;
    }

    storage.removeItem(this.authEmailKey);
  }

  logout(): void {
    this.loggedIn = false;
    this.getSessionStorage()?.removeItem(this.authEmailKey);
  }

  isLoggedIn(): boolean {
    if (this.loggedIn) {
      return true;
    }

    return !!this.getSessionStorage()?.getItem(this.authEmailKey);
  }

  private getSessionStorage(): Storage | null {
    if (typeof window === 'undefined') {
      return null;
    }

    return window.sessionStorage;
  }
}
