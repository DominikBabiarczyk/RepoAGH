export interface PostData {
    donateValue: number,
    cumulativePenaltyValue: number,

    inertia: number,
    c1: number,
    c2: number,
    iterStop: number,
    swarmSize: number,

    expectedProduction: number[],

    productionCostMap: {[s: string]: number},
    storageCostMap: {[s: string]: number},
    truckCostMap: {[s: string]: number}

    // truckCostMap: number[]
}