import React, {createRef, useEffect, useState} from "react"
import { Button, Container, Grid, GridColumn, Header, Segment, Sticky } from "semantic-ui-react"
import { AuthenticatedPageProps } from "../classes/Authentication";
import { Course, FindCourse, GetCode } from "../classes/Course";
import { SearchParams } from "../classes/SearchParams";
import User from "../classes/User";
import { ButtonFooter, ProfileButton } from "../modules/BottomButton";
import ExpandableCourses from "../modules/ExpandableCourses";
import ParamSlider from "../modules/ParamSliders";
import SignOutHeader from "../modules/SignOutHeader";
import {
    BrowserRouter as Router,
    Route,
    Switch,
    Redirect,
    Link
} from "react-router-dom";


interface Params {
    user: User;
    setUser: (user: User | undefined) => void;
    setPath : (path:{[id:string]:number}) => void;
}

const Search: React.FC<Params> = (props) => {
    const [prefs, setPrefs] = useState<SearchParams>(props.user.getPreferences());
    const [takenCourses, setTakenCourses] = useState<Course[]>(props.user.getTaken());
    const [loadingPrefs, setLoading] = useState<boolean>(false);

    useEffect(() => {
        if (loadingPrefs) {
            //TODO: put actual axios request here to get the pathway from backend
            let thePath = {"CSCI 0170":0, "CSCI 0220":1, "CSCI 0180":1, "CSCI 0330":2, "CSCI 1470":2, "CSCI 0320":3, "APMA 0360":5}
            props.setPath(thePath);
            //redirect now to /graph
        }
    }, [loadingPrefs]);

    const setPrefsAsync = async (pref: SearchParams) => setPrefs(pref)

    return <div className="total">
        <ProfileButton />
        <Container >
            <SignOutHeader setUser={props.setUser} user={props.user} />
            <Header as="h1" content={"New search"} />
            <Grid padded stretched centered>
                <Grid.Row stretched>
                    <GridColumn>
                        <Segment>
                            <Header as="h2" content={"Preferences"} />
                            <ParamSlider curUser={props.user} prefChange={setPrefsAsync} />
                        </Segment>
                    </GridColumn>
                </Grid.Row>
                <Grid.Row stretched>
                    <Grid.Column>
                        <ExpandableCourses courses={takenCourses} title={"Taken coures"} modify={{
                            searcher: FindCourse,
                            addCourse: async (course: Course) => setTakenCourses(takenCourses.concat(course)),
                            removeCourse: async (code: string) => setTakenCourses(takenCourses.filter(c => GetCode(c) !== code))
                        }} />
                    </Grid.Column>
                </Grid.Row>
                <Grid.Row stretched>
                    <Grid.Column>
                        <Link to="/graph" onClick={()=>setLoading(true)}>
                            Submit
                        </Link>
                        {/*<Button*/}
                        {/*    loading={loadingPrefs}*/}
                        {/*    content={"Submit"}*/}
                        {/*    className={"gradient"}*/}
                        {/*    />*/}
                    </Grid.Column>
                </Grid.Row>
            </Grid>

            <ButtonFooter />
        </Container>
    </div>
}

export default Search;