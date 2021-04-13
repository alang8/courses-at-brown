import React, { useRef } from "react"
import { Button, Container, Header, Segment, Sticky } from "semantic-ui-react"
import ClassGraph from "../modules/ClassGraph.jsx";
import "../css/Graph.css"
import SignOutHeader from "../modules/SignOutHeader";
import { ProfileButton, SearchButton } from "../modules/BottomButton";
import User from "../classes/User";

//Props interface, pass in the current user, a method to set the user, and the current path taken.
interface Params {
    user: User;
    setUser: (user: User | undefined) => void;
    path : {[code:string]:number};
}

/**
 * Component for the graph displau page.
 * @param props - the Params for this component, as specified above.
 */
const GraphDisplay: React.FC<Params> = (props) => {

    const samplePath = { "CSCI 0170": 0, "CSCI 0220": 1, "CSCI 0180": 1, "CSCI 0330": 2, "CSCI 1470": 2, "CSCI 0320": 3, "APMA 0360": 5 };

    return <div className="total">
        <ProfileButton />
        <SearchButton />
        <SignOutHeader setUser={props.setUser} user={props.user} heading={"Graph"}/>
        <div style={{ height: '100vh' }}>
            <ClassGraph path={samplePath}/>
        </div>
    </div>
}

export default GraphDisplay;