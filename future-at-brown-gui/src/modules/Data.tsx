export interface Course {
    name: string;
    dept: string;
    code: string;
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
}

export const getUser = async (username: string): Promise<User> => {
    await new Promise(resolve => setTimeout(resolve, 3000));
    return {
        username: username,
        saved: [],
        taken: []
    }
}