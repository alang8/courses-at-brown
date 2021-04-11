import React from "react";
import { Button, Grid, Header, Icon, Segment, Sticky } from "semantic-ui-react";
import { AuthenticatedPageProps } from "../classes/Authentication";

const SignOutHeader: React.FC<AuthenticatedPageProps> = (props) => {
    const handleClick = () => {
        localStorage.removeItem("user");
        props.setUser(undefined);
    }
    return <Sticky>
        <Segment clasName="signheader">
            <Grid columns='equal' textAlign="center">
                <Grid.Row>
                    <Grid.Column>
                        <Header as="h2" content={props.user.isGuest ? "" : props.user.username} />
                    </Grid.Column>
                    <Grid.Column>
                        <Header as="h1" className="logo" content="Future @ Brown" />
                    </Grid.Column>
                    <Grid.Column>
                        <Button icon labelPosition='left'
                            compact
                            inverted color="red"
                            onClick={handleClick}>
                            <Icon name="remove user" />
                            {"Sign out"}
                        </Button>
                    </Grid.Column>
                </Grid.Row>
            </Grid>
        </Segment>
    </Sticky>
}

export default SignOutHeader;