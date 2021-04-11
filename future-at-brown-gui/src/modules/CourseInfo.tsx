import React from "react";
import { Button, Grid, Header, Icon, Modal } from 'semantic-ui-react';
import { GetColor } from '../classes/Colors'
import { Course } from '../classes/Course'

interface Params {
    course: Course;
    shouldDisplay: boolean;
    setDisplay: (set: boolean) => void;
    add?: (addCourse: Course) => void;
    remove?: (removeCourse: Course) => void;
    membership?: (testCourse: Course) => boolean;
}

const CourseInfo: React.FC<Params> = (props) => {
    const course: Course = props.course;
    const color = GetColor(course.dept)
    return (
        <Modal
            closeIcon
            open={props.shouldDisplay}
            onClose={() => props.setDisplay(false)}>
            <Modal.Header>
                <Header
                    icon='graduation'
                    content={course.dept + course.code}
                    color={color} />
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
                        <Grid.Column width={10}>
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
                                <Button icon labelPosition='left' className="fill" color={color}>
                                    <Icon name='plus' />
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