import React, { useRef, useState } from "react";
import { Container, Form, Header, Button, Segment } from "semantic-ui-react";
import FormInput from "../modules/FormInput";
import { ValidNewUser, ValidPass } from "../modules/InputValidation";

const Signup: React.FC<{}> = () => {

    const username = useRef<string>("");
    const password = useRef<string>("");
    const confPass = useRef<string>("");

    const [isLoading, setLoading] = useState<boolean>(false);

    const [userError, setUserError] = useState<Array<string>>([]);
    const [passError, setPassError] = useState<Array<string>>([]);
    const [confError, setConfError] = useState<Array<string>>([]);

    const handleSubmit = () => {
        console.log("submitted");
        setLoading(true);
        setPassError(ValidPass(password.current));
        setConfError((password.current !== confPass.current) ? ["passwords much match"] : []);
        ValidNewUser(username.current)
            .then((errors: string[]) => {
                setUserError(errors);
                setLoading(false);
            })
    }

    const checkSuccess = () => {

    }


    return (
        <Container className="total-page">
            <Segment style={{ width: '50%' }}>
                <Header as="h1" content="Sign up" />
                <Form onSubmit={handleSubmit} loading={isLoading}>
                    <FormInput
                        label="username"
                        type="username"
                        textChange={(user: string) => username.current = user}
                        error={{ messages: userError, resolve: () => setUserError([]) }} />
                    <FormInput
                        label="password"
                        type="password"
                        textChange={(pass: string) => password.current = pass}
                        error={{ messages: passError, resolve: () => setPassError([]) }} />
                    <FormInput
                        label="confirm password"
                        type="password"
                        textChange={(pass: string) => confPass.current = pass}
                        error={{ messages: confError, resolve: () => setConfError([]) }} />
                    <Button type="submit" content="Submit" />
                </Form>
            </Segment>
        </Container>
    );

}

export default Signup;