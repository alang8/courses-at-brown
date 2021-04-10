import React, { useState } from "react";
import { Card, Grid, Header, Icon } from 'semantic-ui-react';
import { GetColor } from '../classes/Colors'
import CourseInfo from "./CourseInfo";
import { Course } from "../classes/Course";

interface Params {
    course: Course;
    key?: string;
}

const CourseTile: React.FC<Params> = (props) => {

    const [display, setDisplay] = useState<boolean>(false);

    const course = props.course;

    return (
        <Card className="class-card" color={GetColor(course.dept)} key={props.key}>

            <Card.Content>
                <div onClick={() => setDisplay(true)}>
                    <Grid columns={2}>
                        <Grid.Row >
                            <Grid.Column textAlign='left' width={12}>
                                <Card.Header>
                                    <Header as="h3" color={GetColor(course.dept)}>
                                        {course.dept + course.code}
                                    </Header>
                                </Card.Header>
                            </Grid.Column>
                            <Grid.Column textAlign='right' width={4}>
                                <Icon circular inverted color={GetColor(course.dept)} name='graduation cap' />
                            </Grid.Column>
                        </Grid.Row>
                        <Grid.Row textAlign='right'>
                            <Grid.Column />
                            <Grid.Column verticalAlign='bottom'>
                                <Card.Meta>{course.name}</Card.Meta>
                            </Grid.Column>
                        </Grid.Row>
                    </Grid>
                </div>
            </Card.Content>
            <CourseInfo course={course} setDisplay={setDisplay} shouldDisplay={display} />

        </Card>
    );
}

export default CourseTile;