import React from "react";
import { Link } from "react-router-dom";
import { Button, Container, Header } from 'semantic-ui-react';

const SplashPage: React.FC<{}> = () => {
    return (
        <div className="total-image">
            <Container className="total-page">
                <div className="content">
                    <div>
                        <Header as="h1" className="logo" content="Future @ Brown" />
                        <Header as="h2">
                            Decide how <em>you</em> will take advantage of the open ciricculum
                            </Header>
                    </div>
                    <Button.Group size="massive">
                        <Link to="/signup">
                            <Button content="Sign Up" />
                        </Link>
                        <Button.Or />
                        <Link to="/login">
                            <Button content="Log In" />
                        </Link>
                    </Button.Group>
                </div>
            </Container>
        </div>
    )
}

export default SplashPage;