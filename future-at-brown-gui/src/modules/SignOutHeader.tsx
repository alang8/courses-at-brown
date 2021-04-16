import { Button, Container, Grid, Header, Icon, Segment, Sticky } from "semantic-ui-react";
import { AuthenticatedPageProps } from "../classes/Authentication";
import { SignOutUser } from "../classes/User";
import InfoPopup from "./InfoPopup";

//The props needed for a sign out header - the strings displayed and a boolean if the
// button should displace others or not
interface Props extends AuthenticatedPageProps {
    heading?: {
        title: string;
        information: string;
    }
    dontDisplace?: boolean;
}

/**
 * Module which serves as the header for our page which allows a user to sign out.
 * @param props - the parameters as specified above.
 */
const SignOutHeader: React.FC<Props> = (props) => {

    const header: JSX.Element = <Container>
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
                            onClick={() => SignOutUser(props.setUser)}>
                            <Icon name="sign out" />
                            {"Sign out" + (props.user.isGuest ? " of guest" : "")}
                        </Button>
                    </div>
                </Grid.Column>
            </Grid.Row>
        </Grid>
    </Segment >
</Container>

    return (props.dontDisplace) ?
        <div className={"fixed"} children={header} />
        : <Sticky children={header} />
}

export default SignOutHeader;