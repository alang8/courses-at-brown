import { Course, defaultParams, SearchParamNames, SearchParams } from "./Data";

class User {

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
        await new Promise(resolve => setTimeout(resolve, 2000));
        return this.getSaved();
    }

    getTaken(): Course[] {
        return [...this.taken];
    }

    async removeSaved(toRemove: Course): Promise<Course[]> {
        this.saved.filter((c) => c !== toRemove);
        // TODO:  replace with actuall adding course to dataabase
        await new Promise(resolve => setTimeout(resolve, 2000));
        return this.getSaved();
    }

    // taken courses

    async takeCourse(toAdd: Course): Promise<Course[]> {
        this.taken.push(toAdd);
        // TODO:  replace with actuall adding course to dataabase
        await new Promise(resolve => setTimeout(resolve, 2000));
        return this.getTaken()
    }

    async removeTaken(toRemove: Course): Promise<Course[]> {
        this.taken.filter((c) => c !== toRemove);
        // TODO:  replace with actuall adding course to dataabase
        await new Promise(resolve => setTimeout(resolve, 2000));
        return this.getTaken()
    }

    // preferences

    getPreferences(): SearchParams {
        return {...this.preferences}
    }

    async setPreferences(prefName: SearchParamNames, newVal: number): SearchParams {
        this.preferences[prefName] = newVal;
        // TODO:  replace with actuall adding course to dataabase
        await new Promise(resolve => setTimeout(resolve, 2000));
        return this.getPreferences();
    }

    // getting user from database
    static async getUser(username: string): Promise<User> {
        // TODO: actually get the user from the database
        return new User(
            username,
            [],
            []
        )
    }

}

export default User;