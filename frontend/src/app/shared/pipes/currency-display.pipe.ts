import { formatCurrency } from '@angular/common';
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
	name: 'currencyDisplay',
	standalone: true,
})
export class CurrencyDisplayPipe implements PipeTransform {
	transform(value: number | string | null | undefined, digitsInfo = '1.2-2'): string {
		if (value === null || value === undefined || value === '') {
			return '';
		}

		const amount = typeof value === 'number' ? value : Number(value);
		if (!Number.isFinite(amount)) {
			return '';
		}

		return formatCurrency(amount, 'en-ZA', 'R', 'ZAR', digitsInfo);
	}
}
