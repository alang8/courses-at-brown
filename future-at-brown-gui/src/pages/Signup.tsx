import React, { useRef, useState } from "react";
import { Container, Form, Header, Button, Segment } from "semantic-ui-react";
import FormattedInput from "../modules/FormattedInput";
import { InAuthenticaedPageProps, ValidNewUser, ValidPass } from "../classes/Authentication";
import { newUser } from "../classes/User";
import { HomeButton } from "../modules/BottomButton";
import InfoPopup from "../modules/InfoPopup";

const Signup: React.FC<InAuthenticaedPageProps> = (props) => {

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
                        newUser(username.current, password.current)
                            .then(props.setLogin)
                            .catch(console.log);
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
                        <Button type="submit" content="Submit" className="gradient" />
                    </Form>
                    <InfoPopup message={"User preferences and courses are used to generate concentration plans that are customized for each user. Plans often change, so preferences/courses are saved to make revisiting your path at Brown easy! You can clear your data at anytime through profile settings."} />
                    <div style={{color: "gray", float:'left', marginLeft:10}}>
                        How is my data used?
                    </div>
                </Segment>
            </Container>
        </div>
    );
}

export default Signup;