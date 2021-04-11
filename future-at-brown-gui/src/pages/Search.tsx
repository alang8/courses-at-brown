import React, { createRef, useState } from "react"
import { Button, Container, Grid, GridColumn, Header, Segment, Sticky } from "semantic-ui-react"
import { Course, SearchParams, User } from "../modules/Data"
import ExpandableCourses from "../modules/ExpandableCourses";
import ParamSlider from "../modules/ParamSliders";

interface Params {
    user: User;
}

const Search: React.FC<Params> = (props) => {

    const contextRef = createRef<HTMLElement>();
    const [prefs, setPrefs] = useState<SearchParams>(props.user.preferences);
    const [takenCourses, setTakenCourses] = useState<Course[]>(props.user.taken);

    const setPrefsAsync = async (pref: SearchParams) => setPrefs(pref)

    return <div>
        <Container ref={contextRef.current} textAlign={'center'}>
            <Sticky context={contextRef.current}>
                <Segment><Header as="h1" className="logo" content="Future @ Brown" /></Segment>
            </Sticky>
            <Header as="h1" content={"New search"} />
            <Grid padded stretched centered>
                <Grid.Row stretched>
                    <GridColumn>
                        <Segment>
                            <Header as="h2" content={"Preferences"} />
                            <ParamSlider curUser={props.user} prefChange={setPrefsAsync}/>
                        </Segment>
                    </GridColumn>
                </Grid.Row>
                <Grid.Row stretched>
                    <Grid.Column>
                        <ExpandableCourses courses={takenCourses} title={"Taken courses"} modifiable />
                    </Grid.Column>
                </Grid.Row>
                <Grid.Row stretched>
                    <Grid.Column>
                        <Button content={"Submit"} className={"gradient"} />
                    </Grid.Column>
                </Grid.Row>
            </Grid>
        </Container>
    </div>
}

export default Search;