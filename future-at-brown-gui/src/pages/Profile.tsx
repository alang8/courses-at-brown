import React, { createRef } from "react"
import { Button, Card, Container, Grid, GridColumn, GridRow, Header, Segment, Sticky } from "semantic-ui-react"
import User from "../classes/User";
import ExpandableCourses from "../modules/ExpandableCourses";
import ParamSlider from "../modules/ParamSliders";

interface Params {
    user: User;
}

const Profile: React.FC<Params> = (props) => {

    const contextRef = createRef<HTMLElement>();

    return <div>
        <Container ref={contextRef.current} textAlign={'center'}>
            <Sticky context={contextRef.current}>
                <Segment><Header as="h1" className="logo" content="Future @ Brown" /></Segment>
            </Sticky>
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
        </Container>
    </div>
}

export default Profile;