import { WithdrawalNoticeStatus } from "../enum/withdrawal-status.enum";

export interface StatementCsvRequest{
    productId:string;
    status?:WithdrawalNoticeStatus;
    createdDate?:Date;
    processedDate?:Date;
}