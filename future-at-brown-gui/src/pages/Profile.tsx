import React from "react"
import { Button, Container, Grid, GridColumn, Header, Icon, Rail, Segment, Sticky } from "semantic-ui-react"
import { AuthenticatedPageProps } from "../classes/Authentication";
import { ButtonFooter, SearchButton } from "../modules/BottomButton";
import ExpandableCourses from "../modules/ExpandableCourses";
import ParamSlider from "../modules/ParamSliders";
import SignOutHeader from "../modules/SignOutHeader";

const Profile: React.FC<AuthenticatedPageProps> = (props) => {

    return <Container textAlign={'center'}>
        <SignOutHeader setUser={props.setUser} user={props.user} />
        <Header as="h1" content={"hi, " + props.user.username} />
        <Grid padded stretched centered>
            <Grid.Row stretched>
                <GridColumn>
                    <Segment>
                        <Header as="h2" content={"Preferences"} />
                        <ParamSlider curUser={props.user} />
                        <div style={{ textAlign: "right" }}>
                            <Button content="Save changes" className={"gradient"} />
                        </div>
                    </Segment>
                </GridColumn>
            </Grid.Row>
            <Grid.Row stretched>
                <Grid.Column>
                    <ExpandableCourses courses={props.user.getTaken()} title={"Taken coures"} modifiable />
                </Grid.Column>
            </Grid.Row>
            <Grid.Row stretched>
                <Grid.Column>
                    <ExpandableCourses courses={props.user.getSaved()} title={"Saved coures"} />
                </Grid.Column>
            </Grid.Row>
        </Grid>
        <SearchButton />
        <ButtonFooter />
    </Container>
}

export default Profile;