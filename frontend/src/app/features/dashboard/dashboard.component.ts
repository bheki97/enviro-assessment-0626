import { ChangeDetectorRef, Component, EventEmitter, OnInit, Output } from '@angular/core';
import { DatePipe, NgClass, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { InvestmentPortfolio } from '../../model/investment-portfolio.model';
import { ProductType } from '../../enum/product-type.enum';
import { WithdrawalNoticeStatus, WithdrawalStatus } from '../../enum/withdrawal-status.enum';
import { AuthService } from '../auth/auth.service';
import { ProductListComponent } from './product-list.component';
import { PortfolioApiService } from '../../services/portfolio-api.service';
import { WithdrawalNoticeCreate } from '../../model/withdrawal-notice-create.model';
import { StatementCsvRequest } from '../../model/statement-csv-request.model';
import { finalize } from 'rxjs';
import { CurrencyDisplayPipe } from '../../shared/pipes/currency-display.pipe';
import { DownloadCsvRequest } from './product-list.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [NgIf, NgClass, DatePipe, FormsModule, ProductListComponent, CurrencyDisplayPipe],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent implements OnInit {
  @Output() logout = new EventEmitter<void>();

  constructor(
    private authService: AuthService,
    private router: Router,
    private portfolioApiService: PortfolioApiService,
    private cdr: ChangeDetectorRef
  ) {}

  portfolio: InvestmentPortfolio = {
    id: 'p-1001',
    firstName: 'Bongani',
    lastName: 'Mokoena',
    email: 'bfmokoena7@gmail.com',
    age: 33,
    createdDate: new Date('2023-04-10'),
    products: [
      {
        id: 'prod-1',
        balance: 14500,
        productType: ProductType.RETIREMENT,
        withdrawalNotices: [
          {
            id: 'wd-1',
            amount: 2500,
            status: WithdrawalStatus.PENDING,
            createdDate: new Date('2026-06-01'),
            processedDate: new Date('2026-06-03'),
          },
          {
            id: 'wd-2',
            amount: 1200,
            status: WithdrawalStatus.PROCESSED,
            createdDate: new Date('2026-05-12'),
            processedDate: new Date('2026-05-16'),
          },
        ],
      },
      {
        id: 'prod-2',
        balance: 8200,
        productType: ProductType.EDUCATION,
        withdrawalNotices: [
          {
            id: 'wd-3',
            amount: 900,
            status: WithdrawalStatus.PENDING,
            createdDate: new Date('2026-06-10'),
            processedDate: new Date('2026-06-12'),
          },
        ],
      },
      {
        id: 'prod-3',
        balance: 5600,
        productType: ProductType.BOND,
        withdrawalNotices: [],
      },
    ],
  };

  isWithdrawalModalOpen = false;
  selectedProductForWithdrawal: string | null = null;
  updatedProductId: string | null = null;
  withdrawalAmount: number | null = null;
  feedbackMessage = '';
  feedbackTone: 'success' | 'error' = 'success';
  isSubmitting = false;

  ngOnInit(): void {
    if (this.portfolioApiService.currentPortfolio) {
      this.portfolio = this.normalizePortfolio(this.portfolioApiService.currentPortfolio);
    }
  }

  get totalBalance(): number {
    return this.portfolio.products.reduce((sum, product) => sum + product.balance, 0);
  }

  get pendingWithdrawals(): number {
    return this.portfolio.products.reduce((sum, product) => {
      const pendingAmount = product.withdrawalNotices
        .filter((notice) => this.normalizeStatus(notice.status as WithdrawalStatus | string | number) === WithdrawalStatus.PENDING)
        .reduce((noticeSum, notice) => noticeSum + notice.amount, 0);
      return sum + pendingAmount;
    }, 0);
  }

  get processedWithdrawals(): number {
    return this.portfolio.products.reduce((sum, product) => {
      const processedAmount = product.withdrawalNotices
        .filter((notice) => this.normalizeStatus(notice.status as WithdrawalStatus | string | number) === WithdrawalStatus.PROCESSED)
        .reduce((noticeSum, notice) => noticeSum + notice.amount, 0);
      return sum + processedAmount;
    }, 0);
  }

  openWithdrawalModal(productId: string): void {
    this.selectedProductForWithdrawal = productId;
    this.isWithdrawalModalOpen = true;
    this.withdrawalAmount = null;
    this.feedbackMessage = '';
    this.feedbackTone = 'success';
  }

  closeWithdrawalModal(): void {
    this.isWithdrawalModalOpen = false;
    this.selectedProductForWithdrawal = null;
    this.withdrawalAmount = null;
    console.log('Withdrawal modal closed.');
    this.syncView();
  }

  completeWithdrawal(): void {
    if (this.isSubmitting) {
      return;
    }

    if (!this.selectedProductForWithdrawal) {
      return;
    }

    const amount = Number(this.withdrawalAmount);
    if (!Number.isFinite(amount) || amount <= 0) {
      this.feedbackTone = 'error';
      this.feedbackMessage = 'Please enter a valid withdrawal amount.';
      return;
    }

    const product = this.getProductById(this.selectedProductForWithdrawal);
    if (!product) {
      this.feedbackTone = 'error';
      this.feedbackMessage = 'The selected product could not be found.';
      return;
    }

    this.isSubmitting = true;
    this.syncView();

    const request: WithdrawalNoticeCreate = {
      amount,
      productId: product.id,
      portfolioId: this.portfolio.id,
    };

    this.closeWithdrawalModal();

    this.portfolioApiService.createWithdrawalNotice(request)
      .pipe(finalize(() => {
        this.isSubmitting = false;
        this.syncView();
      }))
      .subscribe({
        next: (notice) => {
          console.log('Withdrawal notice created successfully:', notice);
          const mappedNotice = {
            ...notice,
            status: this.normalizeStatus(notice.status as WithdrawalStatus | string | number),
            createdDate: new Date(notice.createdDate),
            processedDate: notice.processedDate ? new Date(notice.processedDate) : undefined as unknown as Date,
          };

          this.portfolio = {
            ...this.portfolio,
            products: this.portfolio.products.map((entry) => {
              if (entry.id !== product.id) {
                return entry;
              }

              return {
                ...entry,
                withdrawalNotices: [mappedNotice, ...entry.withdrawalNotices],
              };
            }),
          };

          this.updatedProductId = product.id;
          this.feedbackTone = 'success';
          this.feedbackMessage = 'Withdrawal notice created successfully.';
        },
        error: (error) => {
          this.feedbackTone = 'error';
          this.feedbackMessage = error.message || 'Unable to create the withdrawal notice.';
        },
      });
  }

  private normalizePortfolio(portfolio: InvestmentPortfolio): InvestmentPortfolio {
    return {
      ...portfolio,
      createdDate: new Date(portfolio.createdDate),
      products: portfolio.products.map((product) => ({
        ...product,
        withdrawalNotices: product.withdrawalNotices.map((notice) => ({
          ...notice,
          status: this.normalizeStatus(notice.status as WithdrawalStatus | string | number),
          createdDate: new Date(notice.createdDate),
          processedDate: notice.processedDate ? new Date(notice.processedDate) : undefined,
        })),
      })),
    };
  }

  private normalizeStatus(status: WithdrawalStatus | string | number): WithdrawalStatus {
    if (typeof status === 'number') {
      return status in WithdrawalStatus ? status : WithdrawalStatus.CANCELLED;
    }

    const normalized = String(status).trim().toUpperCase();
    switch (normalized) {
      case '0':
      case 'PENDING':
        return WithdrawalStatus.PENDING;
      case '1':
      case 'PROCESSED':
        return WithdrawalStatus.PROCESSED;
      case '2':
      case 'CANCELLED':
        return WithdrawalStatus.CANCELLED;
      default:
        return WithdrawalStatus.CANCELLED;
    }
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

  downloadWithdrawalsCsv(requestInput: DownloadCsvRequest): void {
    const product = this.getProductById(requestInput.productId);
    if (!product) {
      return;
    }

    const statusFilter = requestInput.filters.status;
    const createdDateFilter = requestInput.filters.createdDate;
    const processedDateFilter = requestInput.filters.processedDate;

    const request: StatementCsvRequest = {
      productId: product.id,
      status: statusFilter === '' ? undefined : statusFilter as WithdrawalNoticeStatus,
      createdDate: createdDateFilter ? new Date(createdDateFilter) : undefined,
      processedDate: processedDateFilter ? new Date(processedDateFilter) : undefined,
    };

    this.portfolioApiService.downloadStatementCsv(request).subscribe({
      next: (blob) => {
        if (typeof window === 'undefined') {
          return;
        }

        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `${product.id}-withdrawals.csv`;
        link.click();
        window.URL.revokeObjectURL(url);
        this.feedbackTone = 'success';
        this.feedbackMessage = 'CSV download started.';
      },
      error: (error) => {
        this.feedbackTone = 'error';
        this.feedbackMessage = error.message || 'Unable to download CSV.';
      },
    });
  }

  getProductById(productId: string) {
    return this.portfolio.products.find((product) => product.id === productId);
  }

  onLogout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
    this.logout.emit();
  }

  getStatusLabel(status: WithdrawalStatus): string {
    switch (status) {
      case WithdrawalStatus.PENDING:
        return 'Pending';
      case WithdrawalStatus.PROCESSED:
        return 'Processed';
      case WithdrawalStatus.CANCELLED:
        return 'Cancelled';
      default:
        return 'Unknown';
    }
  }
}
