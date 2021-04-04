import React from "react";
import { Button, Container, Divider, Grid, Header, Segment } from 'semantic-ui-react';

const SplashPage: React.FC<{}> = (props: {}) => {
    return (
        <div className="total">
            <Container className="total-page">
                <div className="content">
                    <div>
                        <Header as="h1" className="logo" content="Future @ Brown" />
                        <Header as="h2">
                            Decide how <em>you</em> will take advantage of the open ciricculum
                            </Header>
                    </div>
                    <Button.Group size="massive">
                        <Button content="Sign Up" />
                        <Button.Or />
                        <Button content="Log In" />
                    </Button.Group>
                    </div>
            </Container>
        </div>
    )
}

export default SplashPage;