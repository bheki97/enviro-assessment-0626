import { WithdrawalStatus } from "../enum/withdrawal-status.enum";

export interface WithdrawalNotice {
    id : string;
    amount: number;
    status: WithdrawalStatus;
    createdDate: Date;
    processedDate?: Date;
}
