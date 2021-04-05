import React, { useState } from "react";
import { Card, Grid, Header, Icon } from 'semantic-ui-react';
import { GetColor } from './Colors'
import CourseInfo from "./CourseInfo";
import { Course } from "./Data";

const CourseTile: React.FC<Course> = (props) => {

    const [display, setDisplay] = useState<boolean>(false);

    console.log(display);

    return (
        <Card className="class-card" color={GetColor(props.dept)} >

            <Card.Content>
                <div onClick={() => setDisplay(true)}>
                    <Grid columns={2}>
                        <Grid.Row >
                            <Grid.Column floated='left' textAlign='left'>
                                <Card.Header>
                                    <Header as="h3" color={GetColor(props.dept)}>
                                        {props.dept + props.code}
                                    </Header>
                                </Card.Header>
                            </Grid.Column>
                            <Grid.Column floated='right' textAlign='right'>
                                <Icon circular inverted color={GetColor(props.dept)} name='graduation cap' />
                            </Grid.Column>
                        </Grid.Row>
                        <Grid.Row textAlign='right'>
                            <Grid.Column />
                            <Grid.Column floated='right' verticalAlign='bottom'>
                                <Card.Meta>{props.name}</Card.Meta>
                            </Grid.Column>
                        </Grid.Row>
                    </Grid>
                </div>
            </Card.Content>
            <CourseInfo course={props} setDisplay={setDisplay} shouldDisplay={display} />

        </Card>
    );
}

export default CourseTile;