import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DashboardComponent } from './dashboard.component';
import { AuthService } from '../auth/auth.service';
import { Router } from '@angular/router';

class MockAuthService {
  loggedIn = true;
  login() {}
  logout() {}
  isLoggedIn() { return this.loggedIn; }
}

class MockRouter {
  navigate = jasmine.createSpy('navigate');
}

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashboardComponent],
      providers: [
        { provide: AuthService, useClass: MockAuthService },
        { provide: Router, useClass: MockRouter },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('adds a new pending withdrawal notice when a withdrawal is completed', () => {
    component.selectedProductId = 'prod-3';
    component.openWithdrawalModal('prod-3');
    component.withdrawalAmount = 1000;
    component.completeWithdrawal();

    const product = component.portfolio.products.find((entry) => entry.id === 'prod-3');
    expect(product?.withdrawalNotices.length).toBe(1);
    expect(product?.withdrawalNotices[0].amount).toBe(1000);
    expect(component.isWithdrawalModalOpen).toBeFalse();
  });
});
