import React, { useRef } from "react"
import { Button, Container, Header, Segment, Sticky } from "semantic-ui-react"
import ClassGraph from "../modules/ClassGraph.jsx";
import { Link } from "react-router-dom";
import "../css/Graph.css"
import { AuthenticatedPageProps } from "../classes/Authentication";
import SignOutHeader from "../modules/SignOutHeader";
import { ProfileButton, SearchButton } from "../modules/BottomButton";
import {Course} from "../classes/Course";
import User from "../classes/User";

interface Params {
    user: User;
    setUser: (user: User | undefined) => void;
    path : {[code:string]:number};
}


const GraphDisplay: React.FC<Params> = (props) => {

    // const samplePath = { "CSCI 0170": 0, "CSCI 0220": 1, "CSCI 0180": 1, "CSCI 0330": 2, "CSCI 1470": 2, "CSCI 0320": 3, "APMA 0360": 5 };

    return <div className="total">
        <ProfileButton />
        <SearchButton />
        <SignOutHeader setUser={props.setUser} user={props.user} heading={"Graph"}/>

        <div style={{ height: '100vh' }}>
            <ClassGraph path={props.path} />
        </div>

    </div>
}

export default GraphDisplay;