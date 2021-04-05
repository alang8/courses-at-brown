import React, { useRef, useState } from "react";
import { Button, Container, Form, Header, Segment } from "semantic-ui-react";
import FormattedInput from "../modules/FormattedInput";
import { ValidPass, ValidUser, ValidLogin } from "../modules/InputValidation";

interface Params{
    setLogin: (user: string) => Promise<any>;
}

const Login: React.FC<Params> = (props) => {
    const username = useRef<string>("");
    const password = useRef<string>("");

    const [isLoading, setLoading] = useState<boolean>(false);

    const [userError, setUserError] = useState<Array<string>>([]);
    const [passError, setPassError] = useState<Array<string>>([]);

    const handleSubmit = () => {
        const passErr: string[] = ValidPass(password.current);
        const userErr: string[] = ValidUser(username.current);
        setLoading(true);
        setPassError(passErr);
        setUserError(userErr);

        if (passErr.length === 0
            && userErr.length === 0) {
        ValidLogin(username.current, password.current)
            .then((loginErr: string[]) => {
                setUserError(loginErr);
                setPassError(loginErr);
                console.log(loginErr);
                if (loginErr.length === 0) {
                    console.log("validated");
                    props.setLogin(username.current)
                        .then(() => setLoading(false));
                } else {
                    setLoading(false);
                }
            })
        } else {
            setLoading(false);
        }
    }

    return (
        <Container className="total-page">
            <Segment style={{ width: '50%' }}>
                <Header as="h1" content="Log in" />
                <Form onSubmit={handleSubmit} loading={isLoading}>
                    <FormattedInput
                        label="username"
                        type="username"
                        textChange={(user: string) => username.current = user}
                        error={{ messages: userError, resolve: () => setUserError([]) }} />
                    <FormattedInput
                        label="password"
                        type="password"
                        textChange={(pass: string) => password.current = pass}
                        error={{ messages: passError, resolve: () => setPassError([]) }} />
                    <Button type="submit" content="Submit" />
                </Form>
            </Segment>
        </Container>
    );
}

export default Login;