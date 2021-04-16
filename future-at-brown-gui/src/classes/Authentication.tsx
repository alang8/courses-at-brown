import axios from "axios";
import User from "./User";

const config = {
    headers: {
        "Content-Type": "application/json",
        'Access-Control-Allow-Origin': '*',
    }
}

//Function to ensure that the inputted string does not contain invalid characters (prevent SQL injection).
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

//Function to ensure that the inputted string is of a certain length.
const certainLength = (length: number): (inp: string) => string => {
    return (inp: string) => {
        if (inp.length >= length) {
            return "";
        } else {
            return "must be at least " + length + " characters long"
        }
    }
}

//Function to ensure that the inputted string isnt blank.
const notBlank = (inp: string): string => {
    if (inp.length === 0) {
        return "cannot be blank"
    } else {
        return "";
    }
}

//Function to ensure that the inputted username isnt already taken.
const takenUsername = async (inp: string): Promise<string> => {
    const toSend = {
        username: inp
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
        isTaken = true;
    });

    if (isTaken) {
        return '"' + inp + '" has already been taken';
    } else {
        return "";
    }
}

//Function to ensure that the inputted username and password are valid for a user.
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
            console.log("Authentication.tsx 87 Login attempt: is valid?")
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

//Function to ensure that the input passes our various tests for valid username/password.
const passTests = (inp: string, tests: Array<(inp: string) => string>): string[] => {
    return tests.reduce((acc: string[], test: (inp: string) => string) => {
        if (test(inp) !== "") {
            acc.push(test(inp))
        }
        return acc;
    }, []);
}

//Function that ensures a password is valid.
export const ValidPass = (pass: string): string[] => {
    return passTests(pass, [withoutChars("\\\"\'"), certainLength(8)]);
}

//Function to ensure that a username is valid.
export const ValidUser = (user: string): string[] => {
    return passTests(user, [withoutChars("\\\"\' !"), notBlank]);
}

//Function that checks if a login attempt is valid.
export const ValidLogin = async (user: string, pass: string): Promise<string[]> => {
    const syncTests = ValidPass(pass).concat(ValidUser(user));
    if (syncTests.length === 0) {
        const valid = await matchingLogin(user, pass);
        if (valid.length > 0) syncTests.push(valid);
    }
    return syncTests;
}

//Function to ensure that a sign up attempt is valid.
export const ValidNewUser = async (user: string): Promise<string[]> => {
    const syncTests = ValidUser(user);
    if (syncTests.length === 0) {
        const valid = await takenUsername(user);
        if (valid.length > 0) syncTests.push(valid);
    }
    return syncTests;
}

//Props for authenticated pages (pages you can only view if signed in).
export interface AuthenticatedPageProps {
    user: User;
    setUser: (user: User | undefined) => void;
}

//Props for Unauthenticated pages (pages anyone can see).
export interface InAuthenticatedPageProps {
    setLogin: (user: User) => void;
}
