import axios from "axios";

export interface Course {
    name: string;
    dept: string;
    code: string;
    prereqs?: string;
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

const config = {
    headers: {
        "Content-Type": "application/json",
        'Access-Control-Allow-Origin': '*',
    }
}

export const FindCourse = async (dept: string, code: string): Promise<Course> => {
    const toSend = {
        dept: dept,
        code: code
    }
    return axios.post(
        'http://localhost:4567/courseinfo',
        toSend,
        config
    )
        .then((course) => course as unknown as Course)
}   