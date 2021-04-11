import axios from "axios";
import User from "./User";

const config = {
    headers: {
        "Content-Type": "application/json",
        'Access-Control-Allow-Origin': '*',
    }
}

const withoutChars = (chars: string): (inp: string) => string => {
    return (inp: string) => {
        const invalid: string =
            chars.split("").reduce((acc, elt) => acc + (inp.indexOf(elt) !== -1 ? elt : ""), "");
        if (invalid.length === 0) {
            return "";
        } else {
            return "not allowed to use any of the following characters: " + chars;
        }
    }
}

const certainLength = (length: number): (inp: string) => string => {
    return (inp: string) => {
        if (inp.length >= length) {
            return "";
        } else {
            return "must be at least " + length + " characters long"
        }
    }
}

const notBlank = (inp: string): string => {
    if (inp.length === 0) {
        return "cannot be blank"
    } else {
        return "";
    }
}

const takenUsername = async (inp: string): Promise<string> => {

    const wait = async (input: string): Promise<boolean> => {
        const toSend = {
            username: input
        };

        let isTaken = true;

        await axios.post(
            'http://localhost:4567/checkname',
            toSend,
            config
        ).then(response => {
            isTaken = response.data["isTaken"];
        }).catch(function (error) {
            console.log(error);
        });
        console.log("in taken username 58")
        console.log(isTaken)
        return isTaken
    }



    return wait(inp)
        .then((isTaken: boolean): string =>
            (isTaken) ? '"' + inp + '" has already been taken' : ""
        );
}

const matchingLogin = async (user: string, pass: string): Promise<string> => {

    const wait = async (u: string, p: string): Promise<boolean> => {
        const toSend = {
            username: u,
            password: p
        };

        let isValid = false;

        await axios.post(
            'http://localhost:4567/login',
            toSend,
            config
        ).then(response => {
            isValid = response.data["isValid"];
            console.log("in then")
            console.log(isValid)
        }).catch(function (error) {
            console.log(error);
        });
        return isValid
    }

    return wait(user, pass)
        .then((isValid: boolean): string =>
            (isValid) ? "" : 'the inputted username/password are incorrect'
        );
}

const passTests = (inp: string, tests: Array<(inp: string) => string>): string[] => {
    return tests.reduce((acc: string[], test: (inp: string) => string) => {
        if (test(inp) !== "") {
            acc.push(test(inp))
        }
        return acc;
    }, []);
}

export const ValidPass = (pass: string): string[] => {
    return passTests(pass, [withoutChars("\\\"\'"), certainLength(8)]);
}

export const ValidUser = (user: string): string[] => {
    return passTests(user, [withoutChars("\\\"\' !"), notBlank]);
}

export const ValidLogin = async (user: string, pass: string): Promise<string[]> => {
    const syncTests = ValidPass(pass).concat(ValidUser(user));
    if (syncTests.length === 0) {
        const valid = await matchingLogin(user, pass);
        if (valid.length > 0) syncTests.push(valid);
    }
    return syncTests;
}

export const ValidNewUser = async (user: string): Promise<string[]> => {
    return Promise.resolve([]);
    const syncTests = ValidUser(user);
    if (syncTests.length === 0) {
        const valid = await takenUsername(user);
        if (valid.length > 0) syncTests.push(valid);
    }
    return syncTests;
}

export interface AuthenticatedPageProps {
    user: User;
    setUser: (user: User | undefined) => void;
}

export interface InAuthenticaedPageProps {
    setLogin: (user: User) => void;
}