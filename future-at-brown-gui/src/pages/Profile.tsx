import { useState, useEffect } from "react"
import { Accordion, Button, Container, Dropdown, Grid, GridColumn, Header, Segment } from "semantic-ui-react"
import { FindCourse } from "../classes/Course";
import { SearchParams } from "../classes/SearchParams";
import { SignOutUser } from "../classes/User";
import { ButtonFooter, SearchButton } from "../modules/BottomButton";
import ExpandableCourses from "../modules/ExpandableCourses";
import ParamSlider from "../modules/ParamSliders";
import SignOutHeader from "../modules/SignOutHeader";
import User from "../classes/User";

//The parameters for our search page: the current user, a method to set the user, and a method to set the path.
interface Params {
    user: User;
    setUser: (user: User | undefined) => void;
}

const Profile: React.FC<Params> = (props) => {

    const [prefs, setPrefs] = useState<SearchParams>(props.user.getPreferences());
    const [loadingPrefs, setLoading] = useState<boolean>(false);
    const [showSettings, setSettings] = useState<boolean>(false);
    const [curRender, setCurRerender] = useState<boolean>(false);

    const forceRerender = () => setCurRerender(!curRender);

    useEffect(() => {
        if (loadingPrefs) {
            props.user.setPreferences(prefs)
                .then(() => setLoading(false));
        }
    }, [loadingPrefs]);

    return <div className="total">
        <SearchButton />
        <Container>
            <SignOutHeader setUser={props.setUser} user={props.user} heading={{
                information: "A place to explore the courses you saved, manage saved user data, "
                    + "and change your default search settings. The SAVED COURSES pane shows courses "
                    + "you've saved after searching. The SEARCH SETTINGS dropdown allows you to modify "
                    + "your default serach settings. The DATA SETTINGS allow you to change and delete "
                    + "what data is stored on the server.",
                title: "Profile"
            }} />
            <Grid padded centered>
                <Grid.Row columns={2}>
                    <Grid.Column textAlign="left">
                        <Header as="h1" content={"hi, " + props.user.username} />
                    </Grid.Column>
                    <Grid.Column textAlign="right">

                        <Dropdown floating text="Data settings " icon="setting">
                            <Dropdown.Menu>
                                <Dropdown.Item text='Clear saved courses' icon='x'
                                    onClick={() =>
                                        props.user.clearSaved().then(forceRerender)
                                    } />
                                <Dropdown.Item text='Clear taken courses' icon='x'
                                    onClick={() =>
                                        props.user.clearTaken().then(forceRerender)
                                    } />
                                <Dropdown.Divider />
                                <Dropdown.Item text='Reset data' icon='refresh'
                                    onClick={() =>
                                        props.user.resetData().then(forceRerender)
                                    } />
                                {(props.user.isGuest) ?
                                    undefined :
                                    <Dropdown.Item text='Delete account' icon='remove user'
                                        onClick={() =>
                                            props.user.deleteUser()
                                                .then(() => SignOutUser(props.setUser))} />}
                            </Dropdown.Menu>
                        </Dropdown>
                    </Grid.Column>
                </Grid.Row>
                <Grid.Row stretched>
                    <Grid.Column>
                        <ExpandableCourses
                            courses={props.user.getSaved()} color={'teal'}
                            title={curRender ? "Saved courses" : "Saved courses"}
                            modify={{
                                searcher: FindCourse,
                                addCourse: e => { return props.user.saveCourse(e); },
                                removeCourse: c => { return props.user.removeSaved(c); }
                            }} key={curRender ? 0 : 1} />
                    </Grid.Column>
                </Grid.Row>
            </Grid>

            <Accordion className="long-accordion">
                <Accordion.Title
                    onClick={() => setSettings(!showSettings)}
                    content={"Show search settings"}
                    active={showSettings} />
                <Accordion.Content
                    active={showSettings}>
                    <Grid padded centered>
                        <Grid.Row stretched>
                            <GridColumn>
                                <Segment color='blue'>
                                    <Header as="h2" content={"Preferences"} />
                                    <ParamSlider curUser={props.user} prefChange={setPrefs} />
                                    <div style={{ textAlign: "right" }}>
                                        <Button
                                            loading={loadingPrefs}
                                            content="Save changes"
                                            className={"gradient"}
                                            onClick={() => setLoading(true)} />
                                    </div>
                                </Segment>
                            </GridColumn>
                        </Grid.Row>
                        <Grid.Row stretched>
                            <Grid.Column>
                                <ExpandableCourses color={'green'}
                                    courses={props.user.getTaken()}
                                    title={"Taken courses"}
                                    modify={{
                                        searcher: FindCourse,
                                        addCourse: e => { return props.user.takeCourse(e); },
                                        removeCourse: c => { return props.user.removeTaken(c); }
                                    }} key={curRender ? 0 : 1} />
                            </Grid.Column>
                        </Grid.Row>
                    </Grid>
                </Accordion.Content>
            </Accordion>

            <ButtonFooter />
        </Container>
    </div >
}

export default Profile;