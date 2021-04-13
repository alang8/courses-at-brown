import axios from "axios";
import { Course, GetCode } from "./Course";
import { defaultParams, SearchParamNames, SearchParams } from "./SearchParams";

const config = {
    headers: {
        "Content-Type": "application/json",
        'Access-Control-Allow-Origin': '*',
    }
}

export const MEMOTY_LOCATION = "user";

class User {

    // accesible variables
    readonly username: string;
    readonly isGuest: boolean;

    private saved: Course[];
    private taken: Course[];
    private preferences: SearchParams;

    constructor();
    constructor(user: string);
    constructor(user: string, saved: Course[], taken: Course[]);
    constructor(user: string | undefined, saved: Course[], taken: Course[], prefs: SearchParams);
    constructor(user?: string, saved?: Course[], taken?: Course[], prefs?: SearchParams) {
        this.username = user || "guest";
        this.saved = [...(saved ?? [])];
        this.taken = [...(taken ?? [])];
        this.preferences = prefs ?? defaultParams;
        if (user) {
            this.isGuest = false;
        } else {
            this.isGuest = true;
        }
    }

    // saved courses

    getSaved(): Course[] {
        return [...this.saved];
    }

    async saveCourse(toSave: Course): Promise<Course[]> {
        this.saved.push(toSave);
        let courseCode = GetCode(toSave);
        const toSend = {
            username: this.username,
            column: "saved_courses",
            course: courseCode
        };

        if (!this.isGuest) {
            await axios.post(
                'http://localhost:4567/writecourse',
                toSend,
                config
            )
                .then((response) => {
                    console.log("saveCourse")
                    console.log(response.data['msg'])
                })
                .catch((error) => {
                    return Promise.reject(error);
                });
        }
        localStorage.setItem(MEMOTY_LOCATION, this.stringify());
        return this.getSaved();
    }

    async clearSaved(): Promise<void> {
        this.saved = [];
        if (!this.isGuest) {
            await new Promise(resolve => setTimeout(resolve, 2000));
        }
        localStorage.setItem(MEMOTY_LOCATION, this.stringify());
    }

    async removeSaved(courseCode: string): Promise<Course[]> {
        this.taken = this.taken.filter((c) => GetCode(c) !== courseCode);

        const toSend = {
            username: this.username,
            column: "saved_courses",
            course: courseCode
        };
        if (!this.isGuest) {
            await axios.post(
                'http://localhost:4567/removecourse',
                toSend,
                config
            )
                .then((response) => {
                    console.log("removeSaved")
                    console.log(response.data['msg'])
                })
                .catch((error) => {
                    return Promise.reject(error);
                });
        }
        localStorage.setItem(MEMOTY_LOCATION, this.stringify());
        return this.getSaved();
    }

    // taken courses

    getTaken(): Course[] {
        return [...this.taken];
    }

    async takeCourse(toAdd: Course): Promise<Course[]> {
        console.log("user", this);
        this.taken.push(toAdd);

        let courseCode = GetCode(toAdd);
        const toSend = {
            username: this.username,
            column: "taken_courses",
            course: courseCode
        };

        if (!this.isGuest) {
            await axios.post(
                'http://localhost:4567/writecourse',
                toSend,
                config
            )
                .then((response) => {
                    console.log("takeCourse")
                    console.log(response.data['msg'])
                })
                .catch((error) => {
                    return Promise.reject(error);
                });
        }

        localStorage.setItem(MEMOTY_LOCATION, this.stringify());
        return this.getTaken()
    }

    async removeTaken(codeToRemove: string): Promise<Course[]> {

        this.taken = this.taken.filter((c) => GetCode(c) !== codeToRemove);

        const toSend = {
            username: this.username,
            column: "taken_courses",
            course: codeToRemove
        };

        if (!this.isGuest) {
            await axios.post(
                'http://localhost:4567/removecourse',
                toSend,
                config
            )
                .then((response) => {
                    console.log("removeTaken")
                    console.log(response.data['msg'])
                })
                .catch((error) => {
                    return Promise.reject(error);
                });
        }
        localStorage.setItem(MEMOTY_LOCATION, this.stringify());
        return this.getTaken()
    }

    async clearTaken(): Promise<void> {
        this.taken = [];
        if (!this.isGuest) {
            await new Promise(resolve => setTimeout(resolve, 2000));
        }
        localStorage.setItem(MEMOTY_LOCATION, this.stringify());
    }

    // preferences

    getPreferences(): SearchParams {
        return { ...this.preferences }
    }

    async setPreferences(prefs: SearchParams): Promise<SearchParams> {
        const toSend = {
            username: this.username,
            pref: prefs,
        };

        if (!this.isGuest) {
            await axios.post(
                'http://localhost:4567/setpreference',
                toSend,
                config
            )
                .then((response) => {
                    console.log("setPreference")
                    console.log(response.data['msg'])
                })
                .catch((error) => {
                    return Promise.reject(error);
                });
        }
        localStorage.setItem(MEMOTY_LOCATION, this.stringify());
        return this.getPreferences();
    }

    resetData() {
        this.clearTaken();
        this.clearSaved();
        this.preferences = defaultParams;
        localStorage.setItem(MEMOTY_LOCATION, this.stringify());
    }

    deleteUser() {
        // do some sql stuff
        localStorage.removeItem("user");
    }

    stringify(): string {
        const jsonVersion = {
            username: (this.isGuest) ? undefined : this.username,
            taken: this.taken,
            saved: this.saved,
            prefs: this.preferences
        }
        console.log(jsonVersion);
        return JSON.stringify(jsonVersion);
    }
}

export default User;

/**
 * Returns the user stored with the given username. Assumes the username is in
 * the database
 * @param username the username of the returned user
 * @returns the user with the given username
 */
export const getUser = async (username: string): Promise<User> => {
    const toSend = {
        username: username
    };

    let savedCourses: Course[] = [];
    let takenCourses: Course[] = [];
    let preferences = defaultParams;

    //response has 'user', 'taken', 'saved'
    await axios.post(
        'http://localhost:4567/loaduser',
        toSend,
        config
    )
        .then((response) => {
            savedCourses = response.data['saved']
            takenCourses = response.data['taken']
            let u = response.data['user']
            preferences = {crsRatingPref: u['crsRatingPref'],
                avgHoursPref: u['avgHoursPref'],
                maxHoursPref: u['maxHoursPref'],
                crsSizePref: u['crsSizePref'],
                profRatingPref: u['profRatingPref']}
        })
        .catch((error) => {
            return Promise.reject(error);
        });

    return new User(
        username,
        savedCourses,
        takenCourses,
        preferences
    )
}

/**
 * Creates a new user with the given username and password. Assumes
 * the username given is unique
 * @param username the username of the new user
 * @param password the password of the new user
 * @returns the new user with the given username
 */
export const newUser = async (username: string, password: string): Promise<User> => {
    const toSend = {
        username: username,
        password: password
    };

    return await axios.post(
        'http://localhost:4567/signup',
        toSend,
        config
    )
        .then(() => {
            return new User(username);
        })
        .catch((error) => {
            return Promise.reject(error);
        });

}

export const destringify = (json: string | null): User | undefined => {
    const value = JSON.parse(json ?? "{}");
    if (value["taken"] && value["saved"] && value["prefs"]) {
        return new User(
            value["username"],
            value["saved"],
            value["taken"],
            value["prefs"]
        )
    } else {
        return undefined;
    }
}