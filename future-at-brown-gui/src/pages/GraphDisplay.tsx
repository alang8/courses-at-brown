import React, {useRef} from "react"
import { Button, Container, Header, Segment, Sticky } from "semantic-ui-react"
import ClassGraph from "../modules/ClassGraph.jsx";
import {Link} from "react-router-dom";
import "../css/Graph.css"
import { AuthenticatedPageProps } from "../classes/Authentication.js";

const GraphDisplay: React.FC<AuthenticatedPageProps> = (props) => {
    const contextRef = useRef();
    let samplePath = {"CSCI 0170":0, "CSCI 0220":1, "CSCI 0180":1, "CSCI 0330":2, "CSCI 1470":2, "CSCI 0320":3, "APMA 0360":5};
    return <div id="container">
        <Button.Group floated="right" size="massive" id="profilebutton">
            <Link to="/profile">
                <Button attached = "right" content="Profile" className="basic"/>
            </Link>
        </Button.Group>
        <Container ref={contextRef.current} textAlign={'center'} style={{height: '200px'}}>
            <Segment attached>
                <Sticky context={contextRef.current}>
                    <Segment><Header as="h1" className="logo" content="Future @ Brown" /></Segment>
                </Sticky>
                <div>
                    <ClassGraph path={samplePath}/>
                </div>
            </Segment>
            <div id="newSearchButton">
                <Button.Group attached="bottom" size="massive">
                    <Link to="/search">
                        <Button content="New Search" className="basic"/>
                    </Link>
                </Button.Group>
            </div>
        </Container>

    </div>
}

export default GraphDisplay;