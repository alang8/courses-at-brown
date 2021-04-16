import React, { createRef, useEffect, useState } from "react"
import ClassGraph from "../modules/ClassGraph.jsx";
import "../css/Graph.css"
import SignOutHeader from "../modules/SignOutHeader";
import { CustomButton, ProfileButton, SearchButton } from "../modules/BottomButton";
import User from "../classes/User";
import { Course } from "../classes/Course";
import { Button, Container, Grid, Header } from "semantic-ui-react";
import { Link } from "react-router-dom";
import { GetColor } from "../classes/Colors";

//Props interface, pass in the current user, a method to set the user, and the current path taken.
interface Params {
    user: User;
    setUser: (user: User | undefined) => void;
    path: { [code: string]: number } | undefined;
    onRender?: () => void
}

/**
 * Component for the graph displau page.
 * @param props - the Params for this component, as specified above.
 */
const GraphDisplay: React.FC<Params> = (props) => {

    const ref = createRef<HTMLElement>();
    const [curDepts, setCurDepts] = useState<string[]>([]);

    useEffect(props.onRender ?? (() => { }), []);

    const getContent = (): JSX.Element => {
        console.log("path", props.path);
        if (props.path) {
            ref.current?.scrollIntoView(true);
            return <ClassGraph
                path={props.path}
                saveFunction={(c: Course) => props.user.saveCourse(c)}
                setRef={ref}
                user={props.user}
                setDepts={setCurDepts} />
        }
        return <Container className={"total-page"}>
            <Header as="h1">
                Uh oh, you haven't started a search yet. Head on over to
                    <Link to="/search" ><code> /search </code></Link> or click
                    the serach button to start a new search!
                    </Header>
        </Container>;
    }

    return <div className="total" style={{ overflow: "hidden" }}>
        <ProfileButton />
        <SearchButton />
        <SignOutHeader setUser={props.setUser} user={props.user} dontDisplace
            heading={{
                title: "Graph",
                information: <div>
                    <Grid divided>
                        <Grid.Row>
                            <Grid.Column>
                                A visual display of the suggested courses you should take at Brown. 
                                Black lines indicate courses in your path, other edges indicate 
                                prerequisite relationships. Nodes/Edges are color-coded by department.
                                A slightly larger edge with more freqent particles to a certain node 
                                means that that class is related to a class currently in the path.
                        </Grid.Column>
                        </Grid.Row>
                        {(curDepts.length > 0) ?
                            <Grid.Row>
                                <Grid.Column>
                                    <strong>{"Color codes of each displayed department"}</strong>
                                </Grid.Column>
                            </Grid.Row> : undefined}
                    </Grid>
                    {(curDepts.length > 0) ?
                        <Grid centered >
                            {curDepts.map(
                                (dept, index) => {
                                    console.log(dept, index);
                                    return <Grid.Row key={index}>
                                        <Grid.Column textAlign={"center"} color={GetColor(dept)}>
                                            {dept}
                                        </Grid.Column>
                                    </Grid.Row>
                                }
                            )}
                        </Grid> : undefined}
                </div>
            }} />
        < div style={{ height: '100vh', width: '100vw' }}>
            {getContent()}
        </div >
    </div >
}

export default GraphDisplay;