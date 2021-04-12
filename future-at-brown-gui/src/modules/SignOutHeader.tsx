import React from "react";
import { Button, Container, Grid, Header, Icon, Segment, Sticky } from "semantic-ui-react";
import { AuthenticatedPageProps } from "../classes/Authentication";

interface Props extends AuthenticatedPageProps {
    heading?: string
}
const SignOutHeader: React.FC<Props> = (props) => {
    const handleClick = () => {
        localStorage.removeItem("user");
        props.setUser(undefined);
    }
    return <Sticky>
        <Container>
            <Segment clasName="signheader">
                <Grid columns='equal' textAlign="center">
                    <Grid.Row verticalAlign="middle">
                        <Grid.Column textAlign="left" >
                            <Header as="h1" className="logo" content="F@B" />
                        </Grid.Column>
                        <Grid.Column textAlign="center">
                            <Header as="h1" content={props.heading} />
                        </Grid.Column>
                        <Grid.Column textAlign="right">
                            <div className="signout-button">
                                <div className="light-header">
                                    {props.user.isGuest ? "" : props.user.username}
                                </div>
                                <Button icon labelPosition='left'
                                    compact
                                    inverted color="red"
                                    onClick={handleClick}>
                                    <Icon name="sign out" />
                                    {"Sign out" + (props.user.isGuest ? " of guest" : "")}
                                </Button>
                            </div>
                        </Grid.Column>
                    </Grid.Row>
                </Grid>
            </Segment >
        </Container>
    </Sticky >
}

export default SignOutHeader;