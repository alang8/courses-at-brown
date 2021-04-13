import React from "react";
import { Button, Container, Grid, Header, Icon, Segment, Sticky } from "semantic-ui-react";
import { AuthenticatedPageProps } from "../classes/Authentication";
import { ClearStoredPath } from "../classes/Path";
import { ClearStoredUser } from "../classes/User";
import InfoPopup from "./InfoPopup";

interface Props extends AuthenticatedPageProps {
    heading?: {
        title: string
        information: string
    }
}
const SignOutHeader: React.FC<Props> = (props) => {

    const handleClick = () => {
        ClearStoredUser();
        ClearStoredPath();
        props.setUser(undefined);
    }

    return <Sticky>
        <Container>
            <Segment className="signheader">
                <Grid columns='equal' textAlign="center">
                    <Grid.Row verticalAlign="middle">
                        <Grid.Column textAlign="left" >
                            <Header as="h1" className="logo" content="F@B" />
                        </Grid.Column>
                        <Grid.Column textAlign="center">
                            <div className="header-title" >
                                <Header as="h1" content={props.heading?.title} />
                                <InfoPopup message={props.heading?.information} />
                            </div>
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