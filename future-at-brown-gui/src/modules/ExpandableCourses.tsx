import React, { useEffect, useState } from "react";
import { Accordion, Button, Card, Header, Icon, Segment } from "semantic-ui-react";
import CourseTile from "./CourseTile";
import { Course, GetCode } from "../classes/Course";
import CourseSearch from "./CourseSearch";

interface Params {
    title: string;
    courses: Course[];
    modify?: {
        searcher: (code: string) => Promise<Course[]>;
        addCourse?: (toAdd: Course) => Promise<any>;
        removeCourse?: (toRemove: string) => Promise<any>;
    }
}

const ExpandableCourses: React.FC<Params> = (props) => {

    const [displayed, setDisplayed] = useState<Course[]>(props.courses);
    const [showMore, setShowMore] = useState<boolean>(false);
    const [isRemoving, setRemoving] = useState<boolean>(false);
    const [isAdding, setAdding] = useState<boolean>(false);

    const allCourses = [...displayed];
    const initDisplay: JSX.Element[] = allCourses.splice(0, 4).map((elt, index) =>
        <CourseTile course={elt} key={String(index)} />
    );

    useEffect(() => {
        setRemoving(false);
        setAdding(false);
    }, [displayed]);

    const remove = async (rmv: Course): Promise<void> => {
        if (props.modify?.removeCourse) {
            await props.modify!.removeCourse(GetCode(rmv));
        }
        setDisplayed(displayed.filter((c) => GetCode(c) !== GetCode(rmv)));
    }

    const add = async (added: Course): Promise<void> => {
        if (props.modify?.addCourse) {
            await props.modify!.addCourse(added);
        }
        setDisplayed(displayed.concat(added));
    }

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

    const overflowCards: JSX.Element[] = allCourses.map(
        (elt, index) => <CourseTile course={elt} key={String(index + initDisplay.length)} />);

    const searchers = ():JSX.Element[] => 
        [<CourseSearch
            searcher={
                async (inp: string) =>
                    displayed.filter((c) => (c.code + c.dept).toLowerCase().indexOf(inp.toLowerCase()) !== -1)}
            resolveButton={{ func: remove, icon: 'x', name: 'Remove course' }} initialResults={displayed}
            setDisplay={setRemoving} shouldDisplay={isRemoving} heading={"Find the course to remove"} />,
        <CourseSearch
            searcher={props.modify!.searcher} resolveButton={{ func: add }}
            setDisplay={setAdding} shouldDisplay={isAdding} heading={"Find a course to add"} />] 

    return (
        <Segment>
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