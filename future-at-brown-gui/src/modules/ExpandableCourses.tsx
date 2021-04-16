import { useEffect, useState } from "react";
import { Accordion, Button, Card, Header, Icon, Segment } from "semantic-ui-react";
import CourseTile from "./CourseTile";
import { Course, GetCode } from "../classes/Course";
import CourseSearch from "./CourseSearch";
import { SemanticCOLORS } from "semantic-ui-react/dist/commonjs/generic";

//The parameters of a course box: the title of the box, the courses to display, and optional functions to add/remove courses.
interface Params {
    title: string;
    courses: Course[];
    modify?: {
        searcher: (code: string) => Promise<Course[]>;
        addCourse?: (toAdd: Course) => Promise<any>;
        removeCourse?: (toRemove: string) => Promise<any>;
    }
    color?: SemanticCOLORS;
}

/**
 * Component which represents the variable size box that holds course tiles for the saved and taken course boxes.
 * @param props - the parameters for a course box, as specified above.
 */
const ExpandableCourses: React.FC<Params> = (props) => {
    //State variables to keep track of the state of the display box.
    const [displayed, setDisplayed] = useState<Course[]>(props.courses);
    const [showMore, setShowMore] = useState<boolean>(false);
    const [isRemoving, setRemoving] = useState<boolean>(false);
    const [isAdding, setAdding] = useState<boolean>(false);

    const alreadyIn = (test: Course): boolean =>
        displayed.find(c => GetCode(c) === GetCode(test)) !== undefined

    const allCourses = [...displayed];
    const initDisplay: JSX.Element[] = allCourses.splice(0, 4).map((elt, index) =>
        <CourseTile course={elt} key={String(index)} shouldDisable={alreadyIn} />
    );

    useEffect(() => {
        setRemoving(false);
        setAdding(false);
    }, [displayed]);

    //Function to remove a course from the box.
    const remove = async (rmv: Course): Promise<void> => {
        if (props.modify?.removeCourse) {
            await props.modify!.removeCourse(GetCode(rmv));
        }
        setDisplayed(displayed.filter((c) => GetCode(c) !== GetCode(rmv)));
    }

    //Function to add a course to the box.
    const add = async (added: Course): Promise<void> => {
        if (!alreadyIn(added)) {
            if (props.modify?.addCourse) {
                await props.modify!.addCourse(added);
            }
            setDisplayed(displayed.concat(added));
        }
    }

    //Buttons for adding/removing courses.
    const addMore: JSX.Element = (
        <Card key="5">
            <Button.Group vertical className="fill" compact>
                <Button icon
                    labelPosition='left'
                    color={'green'}
                    onClick={() => setAdding(true)}>
                    <Icon name='plus' />
                    {"Add"}
                </Button>
                <Button icon
                    labelPosition='left'
                    color={'red'}
                    onClick={() => setRemoving(true)}>
                    <Icon name='x' />
                    {"Remove"}
                </Button>
            </Button.Group>
        </Card >);

    if (props.modify) {
        initDisplay.push(addMore);
    } else if (allCourses.length > 0) {
        initDisplay.push(
            <CourseTile course={allCourses[0]} key={String(5)} />);
        allCourses.shift();
    }

    //Components for all courses, used when show more is clicked
    const overflowCards: JSX.Element[] = allCourses.map(
        (elt, index) => <CourseTile course={elt} key={String(index + initDisplay.length)} shouldDisable={alreadyIn} />);

    //Component for search bars when adding/removing courses.
    const searchers = (): JSX.Element[] =>
        [<CourseSearch key={1} color={'red'}
            searcher={
                async (inp: string) =>
                    displayed.filter((c) => (c.code + c.dept).toLowerCase().indexOf(inp.toLowerCase()) !== -1)}
            resolveButton={{ func: remove, icon: 'x', name: 'Remove course' }} initialResults={displayed}
            setDisplay={setRemoving} shouldDisplay={isRemoving} heading={"Find the course to remove"} />,
        <CourseSearch key={2} shouldDisableCourse={alreadyIn} color={'green'}
            searcher={props.modify!.searcher} resolveButton={{ func: add }}
            setDisplay={setAdding} shouldDisplay={isAdding} heading={"Find a course to add"} />]

    return (
        <Segment color={props.color}>
            {props.modify ? searchers() : undefined}
            {(props.title) ? <Header as="h1" content={props.title} /> : undefined}
            {(initDisplay.length <= 0) ? <Header as="h3" content={"This selection is empty"} />
                : <Card.Group content={initDisplay} centered itemsPerRow={5} />}
            {(overflowCards.length > 0) ?
                <Accordion>
                    <Accordion.Title
                        onClick={() => setShowMore(!showMore)}
                        content={"Show more"}
                        active={showMore}
                    />
                    <Accordion.Content
                        content={<Card.Group content={overflowCards} centered itemsPerRow={5} />}
                        active={showMore}
                    />
                </Accordion>
                : undefined}
        </Segment>
    );
}

export default ExpandableCourses;