import React, { useState } from "react";
import { Card, Grid, Header, Icon, SemanticICONS } from 'semantic-ui-react';
import { GetColor } from '../classes/Colors'
import CourseInfo from "./CourseInfo";
import { Course } from "../classes/Course";

interface Params {
    course: Course;
    key?: string | number;
    isMember?: (testCourse: Course) => boolean;
    onClick?: () => void;
    infoButton?: {
        func: (addCourse: Course) => Promise<void>;
        name?: string;
        icon?: SemanticICONS;
    }
}

const CourseTile: React.FC<Params> = (props) => {

    const [display, setDisplay] = useState<boolean>(false);

    const course = props.course;

    return (
        <Card className="class-card" color={GetColor(course.dept)} key={props.key}>
            <Card.Content>
                <div onClick={props.onClick ?? (() => setDisplay(true))}>
                    <Grid>
                        <Grid.Row >
                            <Grid.Column textAlign='left' width={12} columns={2}>
                                <Card.Header>
                                    <Header as="h3"
                                        color={GetColor(course.dept)}
                                        content={course.dept + course.code} />
                                </Card.Header>
                            </Grid.Column>
                            <Grid.Column textAlign='right' width={4}>
                                <Icon circular inverted
                                    color={GetColor(course.dept)}
                                    name='graduation cap' />
                            </Grid.Column>
                        </Grid.Row>
                        <Grid.Row textAlign='right'>
                            <Grid.Column verticalAlign='bottom'>
                                <div className="left-align">
                                    <Card.Meta content={course.name} />
                                </div>
                            </Grid.Column>
                        </Grid.Row>
                    </Grid>
                </div>
            </Card.Content>
            <CourseInfo course={course}
                setDisplay={setDisplay}
                shouldDisplay={display}
                membership={props.isMember}
                button={props.infoButton}/>
        </Card>
    );
}

export default CourseTile;