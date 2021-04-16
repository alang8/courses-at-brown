import { useState } from "react";
import { Card, Grid, Header, Icon, SemanticICONS } from 'semantic-ui-react';
import { GetColor } from '../classes/Colors'
import CourseInfo from "./CourseInfo";
import { Course, GetCode } from "../classes/Course";

//parameter specification for this component: the course it represents, key (if in a list), optional method for
//dealing with the popup when clicked, optional button for the popup (enabling removal of course from list).
interface Params {
    course: Course;
    key?: string | number;
    shouldDisable?: (testCourse: Course) => boolean;
    onClick?: () => void;
    infoButton?: {
        func: (inp: Course) => Promise<any>;
        name?: string;
        icon?: SemanticICONS;
    }
}

/**
 * Component for a course tile object (for our list of taken and saved courses).
 * @param props - the parameters for the component, as specified above.
 */
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
                                        content={GetCode(course)} />
                                </Card.Header>
                            </Grid.Column>
                            <Grid.Column textAlign='right' width={4}>
                                <Icon circular inverted
                                    color={GetColor(course.dept)}
                                    name='graduation cap' />
                            </Grid.Column>
                        </Grid.Row>
                        <Grid.Row textAlign='right' >
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
                shouldDisable={props.shouldDisable}
                button={props.infoButton}/>
        </Card>
    );
}

export default CourseTile;