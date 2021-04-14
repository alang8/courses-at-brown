//Interface for the parameters that a user has for their search (their preferences for these factors)
export interface SearchParams {
    crsRatingPref: number;
    avgHoursPref: number;
    maxHoursPref: number;
    crsSizePref: number;
    profRatingPref: number;
}

//The valid names for these parameters
export type SearchParamNames =
    "avgHoursPref" | "crsRatingPref" | "maxHoursPref" | "crsSizePref" | "profRatingPref";

//The default parameters (for guests)
export const defaultParams: SearchParams = {
    avgHoursPref: 5,
    crsRatingPref: 5,
    crsSizePref: 5,
    maxHoursPref: 5,
    profRatingPref: 5
}
