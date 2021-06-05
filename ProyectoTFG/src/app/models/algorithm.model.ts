export interface Algorithm {
    name: string;
    color: string;
    params: Params[];
}

export interface Params {
    label: string;
    key: string;
    value: Params;
}