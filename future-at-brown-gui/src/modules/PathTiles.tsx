import React from "react";
import { Card, Grid, Header, Modal, Segment, SemanticCOLORS } from "semantic-ui-react";
import { Course, GetCode } from "../classes/Course";
import User from "../classes/User";
import CourseTile from "./CourseTile";
import ExpandableCourses from "./ExpandableCourses";


interface Params {
    disp?: {
        semester: string;
        classes: Course[]
    }[];
    user: User;
    shouldDisplay: boolean;
    setDisplay: (set: boolean) => void;
}

const PathTiles: React.FC<Params> = (props) => {
    const visible = props.disp?.filter((info) => info.classes.length > 0);
    console.log(props.user);
    return <Modal
        closeIcon
        open={props.shouldDisplay}
        onClose={() => props.setDisplay(false)}>
        <Modal.Header>
            <Header
                icon='sitemap'
                content={"Your suggested path"}
                color={'black'} />
        </Modal.Header>
        <Modal.Content scrolling>
            {(visible) ?
                <Grid padded >
                    {visible!.map(
                        (info, index) => {
                            console.log("info", info);
                            return <Grid.Row key={index}>
                                <Grid.Column>
                                    <Segment color={"black"}>
                                        <Header as="h1" content={info.semester} />
                                        <Card.Group centered content={
                                            info.classes.map((classes, innerIndex) =>
                                                <CourseTile key={innerIndex} course={classes}
                                                    infoButton={{
                                                        func: e => { return props.user.saveCourse(e); }
                                                    }}
                                                    shouldDisable={(test) => props.user
                                                        .getSaved()
                                                        .find(c => GetCode(c) === GetCode(test))
                                                        !== undefined}
                                                />
                                            )
                                        } />
                                    </Segment>
                                </Grid.Column>
                            </Grid.Row>
                        }
                    )}
                </Grid>
                : <Header as="h1" content={"Loading"} />
            }

        </Modal.Content>

    </Modal>
}

export default PathTiles;