import React from "react";
import { Card, Grid, Header, Icon } from 'semantic-ui-react';
import { GetColor } from './Colors'

interface Props {
    department: string;
    code: string;
    title: string;
}

const CourseTile: React.FC<Props> = (props) => {
    return (
        <Card className="class-card" color={GetColor(props.department)}>
            <Card.Content>
                <Grid columns={2}>
                    <Grid.Row >
                        <Grid.Column floated='left' textAlign='left'>
                            <Card.Header>
                                <Header as="h3" color={GetColor(props.department)}>
                                {props.department + props.code}
                                </Header>
                                
                            </Card.Header>
                        </Grid.Column>
                        <Grid.Column floated='right' textAlign='right'>
                            <Icon circular inverted color={GetColor(props.department)} name='graduation cap' />
                        </Grid.Column>
                    </Grid.Row>
                    <Grid.Row textAlign='right'>
                        <Grid.Column />
                        <Grid.Column floated='right' verticalAlign='bottom'>
                            <Card.Meta>{props.title}</Card.Meta>
                        </Grid.Column>
                    </Grid.Row>
                </Grid>
            </Card.Content>
        </Card>
    );
}

export default CourseTile;