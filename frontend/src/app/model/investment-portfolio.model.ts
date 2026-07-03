import { Product } from "./product.model";


export interface InvestmentPortfolio {
    id: string;
    firstName: string;
    lastName: string;
    email: string;
    age: number;
    createdDate: Date;
    products: Product[];
}