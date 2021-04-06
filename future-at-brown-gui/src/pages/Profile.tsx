import React, { createRef } from "react"
import { Card, Container, Grid, GridRow, Header, Segment, Sticky } from "semantic-ui-react"
import CourseInfo from "../modules/CourseInfo";
import CourseTile from "../modules/CourseTile";
import { User } from "../modules/Data"

interface Params {
    user: User;
}

const Profile: React.FC<Params> = (props) => {
    const contextRef = createRef<HTMLElement>();

    return <div>
        <Container ref={contextRef.current} textAlign={'center'} style={{height: '300vh'}}>
            <Sticky context={contextRef.current}>
                <Segment><Header as="h1" className="logo" content="Future @ Brown" /></Segment>
                </Sticky>
            <Header as="h1" content={"hi, " + props.user.username} />
            <Grid padded>
                <Grid.Row>
                    <Segment>
                        <Card.Group>
                            {props.user.saved.map(
                                (course, index) => 
                                <CourseTile key={String(index)} course={course}/>
                            )}
                        </Card.Group>
                    </Segment>
                </Grid.Row>
                <Grid.Row>
                    <Segment>
                        
                    </Segment>
                </Grid.Row>
            </Grid>
        </Container>
    </div>
}

export default Profile;