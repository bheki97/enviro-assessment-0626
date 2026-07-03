import { Component, EventEmitter, Input, Output } from '@angular/core';
import { DatePipe, NgClass, NgFor, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Product } from '../../model/product.model';
import { WithdrawalNoticeStatus, WithdrawalStatus } from '../../enum/withdrawal-status.enum';
import { CurrencyDisplayPipe } from '../../shared/pipes/currency-display.pipe';

export interface ProductCsvFilters {
  status: WithdrawalNoticeStatus | '';
  createdDate: string;
  processedDate: string;
}

export interface DownloadCsvRequest {
  productId: string;
  filters: ProductCsvFilters;
}

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [NgFor, NgIf, NgClass, DatePipe, FormsModule, CurrencyDisplayPipe],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.scss',
})
export class ProductListComponent {
  @Input() products: Product[] = [];
  @Output() withdraw = new EventEmitter<string>();
  @Output() downloadCsv = new EventEmitter<DownloadCsvRequest>();

  selectedProductId: string | null = null;
  filtersByProduct: Record<string, ProductCsvFilters> = {};

  toggleProduct(productId: string): void {
    this.selectedProductId = this.selectedProductId === productId ? null : productId;
  }

  onWithdraw(productId: string): void {
    this.withdraw.emit(productId);
  }

  onDownloadCsv(productId: string): void {
    this.downloadCsv.emit({
      productId,
      filters: { ...this.getFilters(productId) },
    });
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

  getStatusClass(status: WithdrawalStatus): string {
    switch (status) {
      case WithdrawalStatus.PENDING:
        return 'status-pending';
      case WithdrawalStatus.PROCESSED:
        return 'status-processed';
      case WithdrawalStatus.CANCELLED:
        return 'status-cancelled';
      default:
        return '';
    }
  }

  getProductTypeLabel(productType: number): string {
    switch (productType) {
      case 0:
        return 'Cash';
      case 1:
        return 'Education';
      case 2:
        return 'Retirement';
      case 3:
        return 'Bond';
      default:
        return 'Unknown';
    }
  }

  getFilters(productId: string): ProductCsvFilters {
    if (!this.filtersByProduct[productId]) {
      this.filtersByProduct[productId] = { status: '', createdDate: '', processedDate: '' };
    }

    return this.filtersByProduct[productId];
  }

  getFilteredWithdrawals(product: Product): any[] {
    const filters = this.getFilters(product.id);

    return product.withdrawalNotices.filter((notice) => {
      const matchesStatus = filters.status === '' || notice.status === filters.status;
      const matchesCreatedDate = !filters.createdDate || this.matchesDate(notice.createdDate, filters.createdDate);
      const matchesProcessedDate = !filters.processedDate || this.matchesDate(notice.processedDate, filters.processedDate);

      return matchesStatus && matchesCreatedDate && matchesProcessedDate;
    });
  }

  private matchesDate(dateValue: Date | undefined, filterValue: string): boolean {
    if (!dateValue || !filterValue) {
      return true;
    }

    return dateValue.toISOString().slice(0, 10) === filterValue;
  }

  resetFilters(productId: string): void {
    this.filtersByProduct[productId] = { status: '', createdDate: '', processedDate: '' };
  }
}
