import ClassGraph from "../modules/ClassGraph.jsx";
import "../css/Graph.css"
import SignOutHeader from "../modules/SignOutHeader";
import { ProfileButton, SearchButton } from "../modules/BottomButton";
import User from "../classes/User";
import { Course } from "../classes/Course";

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

    props.onRender?.();

    return <div className="total">
        <ProfileButton />
        <SearchButton />
        <SignOutHeader setUser={props.setUser} user={props.user}
            heading={{ title: "Graph", information: "A visual display of the suggested courses you should take at Brown" }} />
        <div style={{ height: '100vh' }}>
            <ClassGraph path={props.path} saveFunction={(c:Course) => props.user.saveCourse(c)} />
        </div>
    </div>
}

export default GraphDisplay;