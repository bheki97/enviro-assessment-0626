import { ProductType } from "../enum/product-type.enum";
import { WithdrawalNotice } from "./withdrawal-notice.model";

export interface Product {
    id: string;
    balance : number;
    productType: ProductType;
    withdrawalNotices: WithdrawalNotice[];
}