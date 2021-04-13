import React, { useState, useEffect, useRef } from "react";
import { Button, Card, Form, Grid, Header, Modal, SemanticICONS } from 'semantic-ui-react';
import { Course } from '../classes/Course'
import CourseTile from "./CourseTile";
import FormattedInput from "./FormattedInput";

/*
Parameters for this component: function to search for a course given a string, boolean and functions to enable popping up,
optional parameters for header, default results, etc.
 */
interface Params {
    searcher: (code: string) => Promise<Course[]>;
    shouldDisplay: boolean;
    setDisplay: (set: boolean) => void;
    resolveButton: {
        func: (clicked: Course) => Promise<void>;
        name?: string;
        icon?: SemanticICONS;
    }
    heading?: string;
    initialResults?: Course[];
    shouldDisableCourse?: (testCourse: Course) => boolean;
}

/**
 * Component for the search window when searching for specific courses to take/save.
 * @param props - the inputs for the component, specified above.
 */
const CourseSearch: React.FC<Params> = (props) => {
    const query = useRef<string>("");
    const [results, setResults] = useState<Course[] | undefined>(undefined);
    const [error, setError] = useState<string | undefined>(undefined);
    const [loading, setLoading] = useState<boolean>(false);

    /**
     * Function to return the html which displays the results of a search.
     */
    const displayResults = (): JSX.Element => {
        const resultCards: JSX.Element[] =
            results!.map((c: Course, index) =>
                <CourseTile course={c}
                    infoButton={props.resolveButton}
                    key={index}
                    shouldDisable={props.shouldDisableCourse} />);
        return <Grid.Row>
            <Grid.Column>
                <Card.Group content={resultCards} />
            </Grid.Column>
        </Grid.Row>
    }

    //If clicked and loading, execute the search.
    useEffect(() => {
        if (loading) {
            setResults(undefined);
            props.searcher(query.current)
                .then((courses) => setResults(courses))
                .catch(() => setError('"' + query.current + '" could not be found'));
            setLoading(false);
        }
    }, [loading])

    //handle popup functionality of search bar.
    useEffect(() => {
        if (props.shouldDisplay) {
            setResults(props.initialResults);
        } else {
            setResults(undefined);
            setLoading(false);
            setError(undefined);
        }
    }, [props.shouldDisplay]);

    return (
        <Modal
            closeIcon
            open={props.shouldDisplay}
            onClose={() => {props.setDisplay(false);}}>
            <Modal.Header>
                <Header content={props.heading ?? "Find a course"} subheading={""} />
                <Form onSubmit={() => setLoading(true)}>
                    <Form.Group className="search-bar" widths="16">
                        <FormattedInput
                            label="search"
                            type="search"
                            textChange={(newStr: string) => query.current = newStr}
                            error={(error) ? { messages: [error!], resolve: () => setError(undefined) } : undefined}
                            width={15} />
                        <Form.Field width={1}>
                            <Button loading={loading} disabled={loading}
                                circular icon="search"
                                className="gradient"
                                type="submit" />
                        </Form.Field>
                    </Form.Group>
                </Form>
            </Modal.Header>
            <Modal.Content scrolling>
                {(results) ? displayResults() : undefined}
            </Modal.Content>
        </Modal>
    );
}

export default CourseSearch;