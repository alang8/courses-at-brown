import React from "react"
import ClassGraph from "../modules/ClassGraph.jsx";
import "../css/Graph.css"
import SignOutHeader from "../modules/SignOutHeader";
import { ProfileButton, SearchButton } from "../modules/BottomButton";
import User from "../classes/User";
import { Header } from "semantic-ui-react";

//Props interface, pass in the current user, a method to set the user, and the current path taken.
interface Params {
    user: User;
    setUser: (user: User | undefined) => void;
    path: { [code: string]: number } | undefined;
    onRender?: () => void
}

/**
 * Component for the graph displau page.
 * @param props - the Params for this component, as specified above.
 */
const GraphDisplay: React.FC<Params> = (props) => {

    const samplePath = { "CSCI 0170": 0, "CSCI 0220": 1, "CSCI 0180": 1, "CSCI 0330": 2, "CSCI 1470": 2, "CSCI 0320": 3, "APMA 0360": 5 };
    props.onRender?.();

    return <div className="total">
        <ProfileButton />
        <SearchButton />
        <SignOutHeader setUser={props.setUser} user={props.user}
            heading={{ title: "Graph", information: "A visual display of the suggested courses you should take at Brown" }} />
        <div style={{ height: '100vh' }}>
            <ClassGraph path={samplePath} />
            {/* {props.path ? <ClassGraph path={props.path!} /> : <Header as="h1" content={"Enter a search to see a graph"} />} */}
        </div>
    </div>
}

export default GraphDisplay;