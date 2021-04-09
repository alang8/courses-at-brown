import React, {createRef, useEffect, useRef, useState} from "react"
import { Button, Container, Grid, GridRow, Header, Segment, Sticky } from "semantic-ui-react"
import { User } from "../modules/Data"
import axios from "axios";
import ClassGraph from "../modules/ClassGraph";
import {Link} from "react-router-dom";
import "../css/Graph.css"

interface Params {
    user: User;
}

const GraphDisplay: React.FC<Params> = (props) => {
    const contextRef = useRef();
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
                    <ClassGraph/>
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