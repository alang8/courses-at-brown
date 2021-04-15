import { createRef } from "react"
import ClassGraph from "../modules/ClassGraph.jsx";
import "../css/Graph.css"
import SignOutHeader from "../modules/SignOutHeader";
import { ProfileButton, SearchButton } from "../modules/BottomButton";
import User from "../classes/User";
import { Course } from "../classes/Course";
import { Container, Header } from "semantic-ui-react";
import { Link } from "react-router-dom";

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

    const ref = createRef<HTMLElement>();

    props.onRender?.();

    const getContent = (): JSX.Element => {
        if (props.path) {
            ref.current?.scrollIntoView(true);
            return <ClassGraph
                path={props.path}
                saveFunction={(c: Course) => props.user.saveCourse(c)}
                ref={ref}
                user={props.user} />
        }
        return <Container textAlign={"center"}>
            <Header as="h1">
                Uh oh, you haven't started a search yet. Head on over to
                    <Link to="/search" ><code> /search </code></Link> or click
                    the serach button to start a new search!
                    </Header>
        </Container>;
    }

    return <div className="total" style={{ overflow: "hidden" }}>
        <ProfileButton />
        <SearchButton />
        <SignOutHeader setUser={props.setUser} user={props.user} dontDisplace
            heading={{ title: "Graph", information: "A visual display of the suggested courses you should take at Brown. Black lines indicate courses in your path, other edges indicate prerequisite relationships. Nodes/Edges are color-coded by department." }} />
        <div style={{ height: '100vh', width: '100vw' }}>
            {getContent()}
        </div>
    </div>
}

export default GraphDisplay;