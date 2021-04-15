import React, { createRef, useEffect, useState } from "react"
import axios from "axios";
import { Button, Container, Dimmer, Grid, GridColumn, Header, Loader, Segment, Sticky } from "semantic-ui-react"
import { Course, FindCourse, GetCode } from "../classes/Course";
import { SearchParams } from "../classes/SearchParams";
import User from "../classes/User";
import { ButtonFooter, ProfileButton } from "../modules/BottomButton";
import ExpandableCourses from "../modules/ExpandableCourses";
import ParamSlider from "../modules/ParamSliders";
import SignOutHeader from "../modules/SignOutHeader";
import { groupEnd } from "node:console";

const config = {
    headers: {
        "Content-Type": "application/json",
        'Access-Control-Allow-Origin': '*',
    }
}

//The parameters for our search page: the current user, a method to set the user, and a method to set the path.
interface Params {
    user: User;
    setUser: (user: User | undefined) => void;
    setPath: (path: { [id: string]: number }) => void;
}

/**
 * Component that serves as the search page of our website.
 * @param props - the useful parameters of the search page, specified above.
 */
const Search: React.FC<Params> = (props) => {

    const [prefs, setPrefs] = useState<SearchParams>(props.user.getPreferences());
    const [takenCourses, setTakenCourses] = useState<Course[]>(props.user.getTaken());
    const [loadingPath, setLoading] = useState<boolean>(false);

    const WrapDiv: React.FC<{}> = (props) =>
        <div className="total" style={{ overflow: loadingPath ? "hidden" : "auto" }}
            children={props.children} />

    //Function to get the path
    const getPath = async (): Promise<void> => {
        const toSend = {
            prefs: prefs,
            concentration: "mathAB"
        };
        console.log("requesting path")
        await axios.post(
            'http://localhost:4567/path',
            toSend,
            config
        )
            .then((response) => {
                console.log("path recieved")
                console.log(response.data['path'])
                props.setPath(response.data['path']);
            })
            .catch((error) => {
                return Promise.reject(error);
            });
    }

    useEffect(() => {
        if (loadingPath) {
            getPath();
        }
    }, [loadingPath]);

    const setPrefsAsync = async (pref: SearchParams) => setPrefs(pref)

    return <Dimmer.Dimmable active={loadingPath} as={WrapDiv}>
        <ProfileButton />
        <SignOutHeader setUser={props.setUser} user={props.user}
            heading={{ title: "Search", information: "This page allows you to put in preferences "
            + "about the path you want to take though Brown (things such as class size, rating, "
            + "etc) so that the algorithm can find your optimal path through a concentration" }} />
        <Dimmer active={loadingPath} blurring>
            <Loader />
        </Dimmer>
        <Container>
            
            <Grid padded stretched centered>
                <Grid.Row>
                    <Grid.Column>
                    <Header as="h1" content={"New search"} />
                    </Grid.Column>
                </Grid.Row>
                <Grid.Row stretched>
                    <Grid.Column>
                        <Segment color="blue">
                            <Header as="h2" content={"Preferences"} />
                            <ParamSlider curUser={props.user} prefChange={setPrefsAsync} />
                        </Segment>
                    </Grid.Column>
                </Grid.Row>
                <Grid.Row stretched>
                    <Grid.Column>
                        <ExpandableCourses courses={takenCourses} title={"Taken coures"} modify={{
                            searcher: FindCourse,
                            addCourse: async (course: Course) => setTakenCourses(takenCourses.concat(course)),
                            removeCourse: async (code: string) => setTakenCourses(takenCourses.filter(c => GetCode(c) !== code))
                        }} color="green" />
                    </Grid.Column>
                </Grid.Row>
                <Grid.Row stretched>
                    <Grid.Column>
                        <Button
                            onClick={() => setLoading(true)}
                            loading={loadingPath}
                            content={"Submit"}
                            className={"gradient"}
                        />
                    </Grid.Column>
                </Grid.Row>
            </Grid>
        </Container>
        <ButtonFooter />
    </Dimmer.Dimmable>
}

export default Search;