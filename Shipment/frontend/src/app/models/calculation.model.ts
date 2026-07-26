
/** Cost breakdown submitted as part of a calculation request. */
export interface Costs {
  costValue: number;
  additionalCostValue: number;
}

/** Payload sent to POST /api/profitLoss/calculation. */
export interface CalculationRequest {
  shipmentReference: string;
  incomeValue: number;
  costs: Costs;
}

/** Result returned by the backend after a profit/loss calculation. */
export interface ProfitLoss {
  shipmentReference: string;
  profitLossValue: number;
  totalIncome: number;
  totalCost: number;
  calculatedOn: string;
}
