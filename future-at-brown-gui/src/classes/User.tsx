import axios from "axios";
import { Course, GetCode } from "./Course";
import { ClearStoredPath } from "./Path";
import { defaultParams, SearchParams } from "./SearchParams";

const config = {
    headers: {
        "Content-Type": "application/json",
        'Access-Control-Allow-Origin': '*',
    }
}

const USER_LOCATION = "user";

/**
 * JS representation of a user of our site.
 */
class User {
    // accesible variables
    readonly username: string;
    readonly isGuest: boolean;

    private saved: Course[] = [];
    private taken: Course[] = [];
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

    //Function to save a course for a given user
    async saveCourse(toSave: Course): Promise<Course[]> {
        if (this.saved === undefined) {
            this.saved = [toSave];
        } else {
            this.saved = [toSave, ...this.saved];
        }

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
        localStorage.setItem(USER_LOCATION, this.stringify());
        return this.getSaved();
    }

    //Function to clear all the saved courses for a user.
    async clearSaved(): Promise<void> {
        let savedArr = this.getSaved();
        for (let c in savedArr) {
            let courseCode = savedArr[c].dept + " " + savedArr[c].code
            await this.removeSaved(courseCode);
        }
        localStorage.setItem(USER_LOCATION, this.stringify());
    }

    //Function to remove a specific course from a users saved course
    async removeSaved(courseCode: string): Promise<Course[]> {
        this.saved = this.saved.filter((c) => GetCode(c) !== courseCode);
        console.log("remove saved taken")
        console.log(this.saved)

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
        localStorage.setItem(USER_LOCATION, this.stringify());
        return this.getSaved();
    }

    // taken courses
    getTaken(): Course[] {
        return [...this.taken];
    }

    //Function to add a course to a users taken courses.
    async takeCourse(toAdd: Course): Promise<Course[]> {
        console.log(this)
        console.log(this.taken)
        // this.taken.push(toAdd);
        if (this.taken === undefined) {
            this.taken = [toAdd];
        } else {
            this.taken = [toAdd, ...this.taken];
        }

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

        localStorage.setItem(USER_LOCATION, this.stringify());
        return this.getTaken()
    }

    //Function to remove a taken course from our user.
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
        localStorage.setItem(USER_LOCATION, this.stringify());
        return this.getTaken()
    }

    //Function to clear a users taken courses
    async clearTaken(): Promise<void> {
        let takenArr = this.getTaken();
        for (let c in takenArr) {
            let courseCode = takenArr[c].dept + " " + takenArr[c].code
            await this.removeTaken(courseCode);
        }
        localStorage.setItem(USER_LOCATION, this.stringify());
    }

    // preferences
    getPreferences(): SearchParams {
        return { ...this.preferences }
    }

    //Function to set a users preferences in the database
    async setPreferences(prefs: SearchParams): Promise<SearchParams> {
        this.preferences = prefs;

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
        localStorage.setItem(USER_LOCATION, this.stringify());
        return this.getPreferences();
    }

    //Function to reset the data to default for to a user.
    async resetData() {
        await this.setPreferences(defaultParams);
        await this.clearSaved()
        await this.clearTaken()
    }

    //Function to delete all the data related to a user from the database.
    async deleteUser(): Promise<void> {
        const toSend = {
            username: this.username
        };

        console.log("in delete user");
        console.log(toSend)

        if (!this.isGuest) {
            await axios.post(
                'http://localhost:4567/deleteuser',
                toSend,
                config
            )
                .then((response) => {
                    console.log("deleted user")
                    console.log(response.data['msg'])
                })
                .catch((error) => {
                    return Promise.reject(error);
                });
        }

        localStorage.removeItem(USER_LOCATION);
    }

    stringify(): string {
        const jsonVersion = {
            username: (this.isGuest) ? undefined : this.username,
            taken: this.taken,
            saved: this.saved,
            prefs: this.preferences
        }
        return JSON.stringify(jsonVersion);
    }

    isIn(where: "taken" | "saved"): (course: Course) => boolean {
        return (course: Course) => {
            const test = (where === "taken") ? this.getSaved() : this.getTaken();
            return test.find((c) => GetCode(c) === GetCode(course)) !== undefined;
        }
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
            preferences = {
                crsRatingPref: u['crsRatingPref'],
                avgHoursPref: u['avgHoursPref'],
                maxHoursPref: u['maxHoursPref'],
                crsSizePref: u['crsSizePref'],
                profRatingPref: u['profRatingPref']
            }
        })
        .catch((error) => {
            return Promise.reject(error);
        });

    const user = new User(
        username,
        savedCourses,
        takenCourses,
        preferences
    );
    localStorage.setItem(USER_LOCATION, user.stringify());
    return user;
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
            const user = new User(username);
            localStorage.setItem(USER_LOCATION, user.stringify());
            return user;
        })
        .catch((error) => {
            return Promise.reject(error);
        });

}

export const GetStoredUser = (): User | undefined => {

    const value = JSON.parse(localStorage.getItem(USER_LOCATION) ?? "{}");

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

//Function to clear the user in memory
export const ClearStoredUser = () => localStorage.removeItem(USER_LOCATION);

//Function to sign a user out of our site.
export const SignOutUser = (userSetter: (user: User | undefined) => void) => {
    ClearStoredUser();
    ClearStoredPath();
    userSetter(undefined);
}