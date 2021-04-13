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
    ).then((course) => {
            if (course.data.dept === "" && course.data.code === "") {
                return Promise.reject();
            } else {
                return Promise.resolve([course.data]);
            }
        })
}   

export const GetCode = (test: Course): string => test.dept + " " + test.code;
