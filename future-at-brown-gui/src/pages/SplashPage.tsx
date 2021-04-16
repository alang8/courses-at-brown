import { Link } from "react-router-dom";
import { Button, Container, Header } from 'semantic-ui-react';
import { InAuthenticatedPageProps } from "../classes/Authentication";
import User from "../classes/User";

/**
 * Our splash page for the site, where users start their experience!
 * @param props -  the props for an inauthenticated page.
 */
const SplashPage: React.FC<InAuthenticatedPageProps> = (props) => {
    return (
        <div className="total image">
            <Container className="total-page">
                <div className="content">
                    <div>
                        <Header as="h1" className="logo" content="Future @ Brown" />
                        <Header as="h2">
                            Decide how <em>you</em> will take advantage of the open curriculum
                            </Header>
                    </div>
                    <Button.Group size="massive">
                        <Link to="/login">
                            <Button
                                content="Log in"
                                className="gradient" />
                        </Link>
                        <Button.Or />
                        <Button
                            content="Continue as Guest"
                            className="gradient"
                            onClick={() => {
                                console.log("clicked")
                                props.setLogin(new User())}} />
                    </Button.Group>
                    <Header as="h3">
                        {"Don't have an account? Sign up "}
                        <Link to="/signup">
                            {"Here"}
                        </Link>
                    </Header>
                </div>
            </Container>
        </div>
    )
}

export default SplashPage;