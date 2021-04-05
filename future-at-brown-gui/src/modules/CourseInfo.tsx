import React from "react";
import { useParams } from "react-router";
import { Button, Card, Grid, GridRow, Header, Icon, Modal } from 'semantic-ui-react';
import { GetColor } from './Colors'
import { Course } from "./Data";

interface Params {
    course: Course;
    shouldDisplay: boolean;
    setDisplay: (set: boolean) => void;
}

const CourseInfo: React.FC<Params> = (props) => {
    const course: Course = props.course;
    console.log("display", props.shouldDisplay);
    return (
        <Modal
            closeIcon
            open={props.shouldDisplay}
            onClose={() => props.setDisplay(false)}>
            <Modal.Header>
                <Header
                    icon='graduation'
                    content={course.dept + course.code}
                    color={GetColor(course.dept)} />
            </Modal.Header>

            <Modal.Content>
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
                        <Grid.Column width={12}>
                            <Grid.Row>
                                <p>
                                    <Icon name="star" />
                                    <strong>Rating: </strong>
                                    {course.rating ?? "N/A"}
                                </p>
                            </Grid.Row>
                            <Grid.Row>
                                <p>
                                <Icon name="graduation" />
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
                        <Grid.Column width={4} textAlign="center">
                            <Grid.Row centered>
                                <Button icon labelPosition='left' className="fill">
                                    <Icon name='plus'/>
                                    {"Save Course"}
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