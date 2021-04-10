import { stringify } from "node:querystring"

export interface Course {
    name: string;
    dept: string;
    code: string;
    prereqs?: string[];
    description?: string;
    rating?: number;
    latestProf?: string;
    latestProfRating?: number;
    maxHours?: number;
    avgHours?: number;
}

export interface User {
    username: string;
    saved: Course[];
    taken: Course[];
    preferences: SearchParams;
    isGuest?: boolean;
}

export const newGuest = ():User => {
    return {
        username: "guest",
        saved: [],
        taken: [],
        preferences: defaultParams,
        isGuest: true
    }
}

export const newUser = (name: string):User => {
    return {
        username: name,
        saved: [],
        taken: [],
        preferences: defaultParams
    }
}
export interface SearchParams {
    crsRatingPref: number;
    avgHoursPref: number;
    maxHoursPref: number;
    crsSizePref: number;
    profRatingPref: number;
}

export const defaultParams: SearchParams = {
    avgHoursPref: 5,
    crsRatingPref: 5,
    crsSizePref: 5,
    maxHoursPref: 5,
    profRatingPref: 5
}

export const getUser = async (username: string): Promise<User> => {
    await new Promise(resolve => setTimeout(resolve, 3000));
    return {
        username: username,
        saved: [],
        taken: [],
        preferences: defaultParams
    }
}