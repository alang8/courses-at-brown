import React from "react"
import { Button, Container, Dropdown, Grid, GridColumn, Header, Icon, Rail, Segment, Sticky } from "semantic-ui-react"
import { AuthenticatedPageProps } from "../classes/Authentication";
import { ButtonFooter, SearchButton } from "../modules/BottomButton";
import ExpandableCourses from "../modules/ExpandableCourses";
import ParamSlider from "../modules/ParamSliders";
import SignOutHeader from "../modules/SignOutHeader";

const Profile: React.FC<AuthenticatedPageProps> = (props) => {

    return <div className="total">
        <SearchButton />
        <Container>
            <SignOutHeader setUser={props.setUser} user={props.user} heading={"Profile"}/>
            <Grid padded centered>
                <Grid.Row columns={2}>
                    <Grid.Column textAlign="left">
                        <Header as="h1" content={"hi, " + props.user.username} />
                    </Grid.Column>
                    <Grid.Column textAlign="right">

                        <Dropdown floating text="Settings " icon="setting">
                        <Dropdown.Menu>
                            <Dropdown.Item text='Clear saved courses' icon='x'/>
                            <Dropdown.Item text='Clear taken courses' icon='x'/>
                            <Dropdown.Divider />
                            <Dropdown.Item text='Reset data' icon='refresh'/>
                            <Dropdown.Item text='Delete account' icon='remove user' />
                        </Dropdown.Menu>
                        </Dropdown>
                    </Grid.Column>
                </Grid.Row>
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
        <ButtonFooter />
        </Container>
    </div >
}

export default Profile;