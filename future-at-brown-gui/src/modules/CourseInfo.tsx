import React, { useState, useEffect } from "react";
import { Button, Grid, Header, Icon, Modal, SemanticICONS } from 'semantic-ui-react';
import { GetColor } from '../classes/Colors'
import { Course, GetCode } from '../classes/Course'

//Format for the input parameters for a CourseInfo component, the course information, boolean representing
// if should be visible, setter for that boolean, optional functions for disabling and other utility buttons (ex 'save course')
interface Params {
    course: Course;
    shouldDisplay: boolean;
    setDisplay: (set: boolean) => void;
    shouldDisable?: (testCourse: Course) => boolean;
    button?: {
        func: (addCourse: Course) => Promise<any>;
        name?: string;
        icon?: SemanticICONS;
    }
}

/**
 * Component which represents a popup displaying all of a courses information.
 * @param props - all of the information we have about the course/display, formatted as described above.
 */
const CourseInfo: React.FC<Params> = (props) => {
    const course: Course = props.course;
    const color = GetColor(course.dept);
    const [loading, setLoading] = useState<boolean>(false);

    //If button is clicked, set button to loading and execute optional button functionality.
    useEffect(() => {
        if (loading) {
            if (props.button) {
                props.button!.func(course)
                    .then(() => setLoading(false));
            } else {
                setLoading(false);
            }
        }
    }, [loading])

    return (
        <Modal
            closeIcon
            open={props.shouldDisplay}
            onClose={() => props.setDisplay(false)}>
            <Modal.Header>
                <Header
                    icon='graduation'
                    content={GetCode(course)}
                    color={color} />
            </Modal.Header>

            <Modal.Content scrolling>
                <Grid padded relaxed="very" stretched>
                    <Grid.Row centered>
                        <Header as="h3"><em>{course.name}</em></Header>
                    </Grid.Row>
                    <Grid.Row>
                        <Grid.Column>
                            <p>{course.description ?? "Description not provided"}</p>
                        </Grid.Column>

                    </Grid.Row>
                    <Grid.Row divided>
                        <Grid.Column width={10}>
                            <Grid.Row>
                                <p>
                                    <Icon name="sitemap" />
                                    <strong>Prereqs: </strong>
                                    {(!course.prereqs || course.prereqs.length === 0) ?
                                        "None (or none listed)" : course.prereqs}
                                </p>
                            </Grid.Row>
                            <Grid.Row>
                                <p>
                                    <Icon name="star" />
                                    <strong>Rating: </strong>
                                    {course.rating ?? "N/A"}
                                </p>
                            </Grid.Row>
                            <Grid.Row>
                                <p>
                                    <Icon name="user circle" />
                                    <strong>Latest Professor: </strong>
                                    {course.latestProf ?? "N/A"}
                                </p>
                            </Grid.Row>
                            <Grid.Row>
                                <p>
                                    <Icon name="heart" />
                                    <strong>Professor Rating: </strong>
                                    {course.latestProfRating ?? "N/A"}
                                </p>
                            </Grid.Row>
                            <Grid.Row>
                                <p>
                                    <Icon name="clock" />
                                    <strong>Average Hours: </strong>
                                    {course.avgHours ?? "N/A"}
                                </p>
                            </Grid.Row>
                            <Grid.Row>
                                <p>
                                    <Icon name="hourglass" />
                                    <strong>Maximum Hours: </strong>
                                    {course.maxHours ?? "N/A"}
                                </p>
                            </Grid.Row>
                        </Grid.Column>
                        <Grid.Column width={6} textAlign="center">
                            <Grid.Row centered>
                                <Button icon
                                        disabled={props.shouldDisable?.(course)}
                                        loading={loading}
                                        labelPosition='left'
                                        className="fill" color={color}
                                        onClick={() => setLoading(true)}>
                                    <Icon name={props.button?.icon ?? 'add'} />
                                    {props.button?.name ?? "Save Course"}
                                </Button>
                            </Grid.Row>
                        </Grid.Column>
                    </Grid.Row>
                </Grid>
            </Modal.Content>
        </Modal>
    );
}

export default CourseInfo;