import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map, timeout } from 'rxjs/operators';
import { InvestmentPortfolio } from '../model/investment-portfolio.model';
import { WithdrawalNotice } from '../model/withdrawal-notice.model';
import { WithdrawalNoticeCreate } from '../model/withdrawal-notice-create.model';
import { StatementCsvRequest } from '../model/statement-csv-request.model';
import { WithdrawalStatus } from '../enum/withdrawal-status.enum';

export interface ApiResponse<T> {
  data?: T;
  message?: string;
  success: boolean;
}

@Injectable({ providedIn: 'root' })
export class PortfolioApiService {
  private readonly baseUrl = 'http://localhost:8081/api/v1/investments/';
  currentPortfolio: InvestmentPortfolio | null = null;

  constructor(private http: HttpClient) {}

  setCurrentPortfolio(portfolio: InvestmentPortfolio | null): void {
    this.currentPortfolio = portfolio;
  }

  login(email: string): Observable<InvestmentPortfolio> {
    console.log(`Attempting to log in with email: ${email}`);
    return this.http.get<ApiResponse<InvestmentPortfolio> | InvestmentPortfolio>(`${this.baseUrl}portfolio/${email}`).pipe(
      timeout(5000),
      map((response: ApiResponse<InvestmentPortfolio> | InvestmentPortfolio) => this.extractData<InvestmentPortfolio>(response, 'No portfolio found for this email.')),
      catchError((error: unknown) => this.handleError<InvestmentPortfolio>(error, 'Service unavailable. Please try again later.'))
    );
  }

  createWithdrawalNotice(request: WithdrawalNoticeCreate): Observable<WithdrawalNotice> {
    return this.http.post<ApiResponse<WithdrawalNotice> | WithdrawalNotice>(`${this.baseUrl}withdrawal-notice`, request).pipe(
      map((response: ApiResponse<WithdrawalNotice> | WithdrawalNotice) => this.extractData<WithdrawalNotice>(response, 'Unable to create withdrawal notice.')),
      catchError((error: unknown) => this.handleError<WithdrawalNotice>(error, 'Unable to create withdrawal notice.'))
    );
  }

  downloadStatementCsv(request: StatementCsvRequest): Observable<Blob> {
    let params = new HttpParams().set('productId', request.productId);

    if (request.status !== undefined) {
      params = params.set('status', this.toStatusParam(request.status));
    }

    if (request.createdDate) {
      params = params.set('createdDate', this.toDateString(request.createdDate));
    }

    if (request.processedDate) {
      params = params.set('processedDate', this.toDateString(request.processedDate));
    }

    return this.http.get(`${this.baseUrl}withdrawal-notice/csv`, {
      params,
      responseType: 'blob',
      observe: 'response',
    }).pipe(
      map((response) => response.body as Blob),
      catchError((error: unknown) => this.handleError<Blob>(error, 'Unable to download CSV statement.'))
    );
  }

  private extractData<T>(response: ApiResponse<T> | T, fallbackMessage: string): T {
    if (response && typeof response === 'object' && 'data' in response) {
      const apiResponse = response as ApiResponse<T>;
      if (apiResponse.success === false || apiResponse.data === undefined || apiResponse.data === null) {
        throw new Error(apiResponse.message || fallbackMessage);
      }

      return apiResponse.data;
    }

    return response as T;
  }

  private handleError<T>(error: unknown, fallbackMessage: string): Observable<T> {
    let message = fallbackMessage;

    if (error instanceof HttpErrorResponse) {
      if (typeof error.error === 'string' && error.error.trim().length > 0) {
        message = error.error;
      } else {
        message = error?.error?.message || error?.error?.error?.message || error?.message || fallbackMessage;
      }
    } else if (error instanceof Error) {
      message = error.message || fallbackMessage;
    }

    console.error(message);
    return throwError(() => new Error(message));
  }

  private toDateString(date: Date): string {
    return new Date(date).toISOString().slice(0, 10);
  }

  private toStatusParam(status: StatementCsvRequest['status']): string {
    if (status === undefined) {
      return '';
    }

    return WithdrawalStatus[status] ?? String(status);
  }
}
