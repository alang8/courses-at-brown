import { Course, defaultParams, SearchParams } from "./Data";

class User {

    readonly username: string;
    readonly saved: Course[];
    readonly taken: Course[];
    readonly preferences: SearchParams;
    readonly isGuest: boolean;

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

    getSaved(): Course[] {
        return [...this.saved];
    }

    getTaken(): Course[] {
        return [...this.taken];
    }

    async takeCourse(toAdd: Course): Promise<void> {
        this.taken.push(toAdd);
        // TODO:  replace with actuall adding course to dataabase
        return new Promise(resolve => setTimeout(resolve, 2000));
    }

    async saveCourse(toSave: Course): Promise<void> {
        this.saved.push(toSave);
        // TODO:  replace with actuall adding course to dataabase
        return new Promise(resolve => setTimeout(resolve, 2000));
    }

}

export default User;