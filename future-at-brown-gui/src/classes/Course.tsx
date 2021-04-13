import axios from "axios";

//Interface for a course object- all the info we have about each course.
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

/**
 * Method for finding all the course info given a user's search string.
 * @param inp - the inputted search string.
 */
export const FindCourse = async (inp: string): Promise<Course[]> => {
    const sanatized: string = inp.replaceAll(/[^a-z0-9A-Z]+/gm, '');
    const toSend = {
        dept: sanatized.substring(0, 4).toUpperCase(),
        code: sanatized.substring(4).toUpperCase()
    }
    console.log("sending", toSend);
    return await axios.post<Course>(
        'http://localhost:4567/courseinfo',
        toSend,
        config
    ).then((course) => (course.data.dept.length !== 0 && course.data.code.length !== 0)
         ? Promise.resolve([course.data]) : Promise.reject());
}   

export const GetCode = (test: Course): string => test.dept.trim() + " " + test.code.trim();
