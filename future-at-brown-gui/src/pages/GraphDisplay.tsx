import React, { useRef } from "react"
import { Button, Container, Header, Segment, Sticky } from "semantic-ui-react"
import ClassGraph from "../modules/ClassGraph.jsx";
import { Link } from "react-router-dom";
import "../css/Graph.css"
import { AuthenticatedPageProps } from "../classes/Authentication.js";
import SignOutHeader from "../modules/SignOutHeader.js";
import { ProfileButton, SearchButton } from "../modules/BottomButton.js";

const GraphDisplay: React.FC<AuthenticatedPageProps> = (props) => {

    const samplePath = { "CSCI 0170": 0, "CSCI 0220": 1, "CSCI 0180": 1, "CSCI 0330": 2, "CSCI 1470": 2, "CSCI 0320": 3, "APMA 0360": 5 };

    return <Container style={{ height: '100vh' }}>
        <SignOutHeader setUser={props.setUser} user={props.user} />
        <div style={{ height: '90vh' }}>
            <ClassGraph path={samplePath} />
        </div>
        <ProfileButton />
        <SearchButton />
    </Container>
}

export default GraphDisplay;