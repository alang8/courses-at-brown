import React, { useRef, useState } from "react";
import { Container, Form, Header, Button, Segment } from "semantic-ui-react";
import FormattedInput from "../modules/FormattedInput";
import { ValidNewUser, ValidPass } from "../modules/InputValidation";
import axios from "axios";

interface Props {
    setLogin: (user: string) => Promise<any>;
}

let config = {
    headers: {
        "Content-Type": "application/json",
        'Access-Control-Allow-Origin': '*',
    }
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
                    //want to axios write username data in here
                    const toSend = {
                        username: username.current,
                        password: password.current
                    };

                    axios.post(
                        'http://localhost:4567/signup',
                        toSend,
                        config
                    )
                        .then(response => {
                            console.log("resp :")
                            console.log(response)
                        })
                        .catch(function (error) {
                            console.log(error);
                        });

                    props.setLogin(username.current)
                } else {
                    setLoading(false);
                }
            })
        } else {
            setLoading(false);
        }
    }

    return (
        <div className="total-grad">
        <Container className="total-page">
            <Header as="h1" className="logo" content="Future @ Brown" />
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
                    <Button type="submit" content="Submit" className="gradient"/>
                </Form>
            </Segment>
        </Container>
        </div>
    );
}

export default Signup;