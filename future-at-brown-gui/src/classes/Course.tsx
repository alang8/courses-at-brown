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

export type CourseInventory = {
    [course: string]: Course;
}
