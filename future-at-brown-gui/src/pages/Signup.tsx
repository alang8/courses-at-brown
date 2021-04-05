import React, { useRef, useState } from "react";
import { Container, Form, Header, Button, Segment } from "semantic-ui-react";
import FormattedInput from "../modules/FormattedInput";
import { ValidNewUser, ValidPass, ValidUser } from "../modules/InputValidation";

interface Props {
    setLogin: (user: string) => Promise<any>;
}

const Signup: React.FC<Props> = (props) => {

    const username = useRef<string>("");
    const password = useRef<string>("");
    const confPass = useRef<string>("");

    const [isLoading, setLoading] = useState<boolean>(false);

    const [userError, setUserError] = useState<Array<string>>([]);
    const [passError, setPassError] = useState<Array<string>>([]);
    const [confError, setConfError] = useState<Array<string>>([]);

    const handleSubmit = () => {
        const passErr: string[] = ValidPass(password.current);
        const confErr: string[] =
            (password.current !== confPass.current) ? ["passwords much match"] : [];
        setLoading(true);
        setPassError(passErr);
        setConfError(confErr);
        if (passErr.length === 0
            && confErr.length === 0) {
        ValidNewUser(username.current)
            .then((userErr: string[]) => {
                setUserError(userErr);
                if (userErr.length === 0) {
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
                <Header as="h1" content="Sign up" />
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
                    <FormattedInput
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