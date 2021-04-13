import React, { useRef, useState } from "react";
import { Button, Container, Form, Header, Segment } from "semantic-ui-react";
import FormattedInput from "../modules/FormattedInput";
import { ValidPass, ValidUser, ValidLogin, InAuthenticaedPageProps } from "../classes/Authentication";
import { getUser } from "../classes/User";
import { HomeButton } from "../modules/BottomButton";

const Login: React.FC<InAuthenticaedPageProps> = (props) => {
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
                    if (loginErr.length === 0) {
                        getUser(username.current)
                            .then((r) => {
                                console.log("in login.tsx 33");
                                console.log(r);
                                props.setLogin(r);
                            })
                            .catch(console.log)
                    } else {
                        setLoading(false);
                    }
                })
        } else {
            setLoading(false);
        }
    }

    return (
        <div className="total grad">
            <HomeButton />
            <Container className="total-page">
                <Header as="h1" className="logo" content="Future @ Brown" />
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
                        <Button type="submit" content="Submit" className="gradient" />
                    </Form>
                </Segment>
            </Container>
        </div>
    );
}

export default Login;