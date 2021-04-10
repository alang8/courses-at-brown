import axios from "axios";
import { Course } from "./Course";
import { defaultParams, SearchParamNames, SearchParams } from "./SearchParams";

const config = {
    headers: {
        "Content-Type": "application/json",
        'Access-Control-Allow-Origin': '*',
    }
}

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
    constructor(user?: string, saved?: Course[], taken?: Course[]) {
        this.username = user || "guest";
        this.saved = [...(saved ?? [])];
        this.taken = [...(taken ?? [])];
        this.preferences = defaultParams;
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
        // TODO:  replace with actuall adding course to dataabase
        if (!this.isGuest) {
            await new Promise(resolve => setTimeout(resolve, 2000));
        }
        return this.getSaved();
    }

    getTaken(): Course[] {
        return [...this.taken];
    }

    async removeSaved(toRemove: Course): Promise<Course[]> {
        this.saved.filter((c) => c !== toRemove);
        // TODO:  replace with actuall adding course to dataabase
        if (!this.isGuest) {
            await new Promise(resolve => setTimeout(resolve, 2000));
        }
        return this.getSaved();
    }

    // taken courses

    async takeCourse(toAdd: Course): Promise<Course[]> {
        this.taken.push(toAdd);
        // TODO:  replace with actuall adding course to dataabase
        if (!this.isGuest) {
            await new Promise(resolve => setTimeout(resolve, 2000));
        }
        return this.getTaken()
    }

    async removeTaken(toRemove: Course): Promise<Course[]> {
        this.taken.filter((c) => c !== toRemove);
        // TODO:  replace with actuall adding course to dataabase
        if (!this.isGuest) {
            await new Promise(resolve => setTimeout(resolve, 2000));
        };
        return this.getTaken()
    }

    // preferences

    getPreferences(): SearchParams {
        return { ...this.preferences }
    }

    async setPreferences(prefName: SearchParamNames, newVal: number): Promise<SearchParams> {
        this.preferences[prefName] = newVal;
        // TODO:  replace with actuall adding course to dataabase
        await new Promise(resolve => setTimeout(resolve, 2000));
        return this.getPreferences();
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
    // TODO: actually get the user from the database
    return new User(
        username,
        [],
        []
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

    return axios.post(
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