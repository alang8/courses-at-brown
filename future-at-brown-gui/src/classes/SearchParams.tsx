export interface SearchParams {
    crsRatingPref: number;
    avgHoursPref: number;
    maxHoursPref: number;
    crsSizePref: number;
    profRatingPref: number;
}

export type SearchParamNames =
    "avgHoursPref" | "crsRatingPref" | "maxHoursPref" | "crsSizePref" | "profRatingPref";

export const defaultParams: SearchParams = {
    avgHoursPref: 5,
    crsRatingPref: 5,
    crsSizePref: 5,
    maxHoursPref: 5,
    profRatingPref: 5
}
