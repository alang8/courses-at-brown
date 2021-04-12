import React, { useState, useEffect, useRef } from "react";
import { Button, Card, Grid, Header, Modal } from 'semantic-ui-react';
import { Course } from '../classes/Course'
import CourseTile from "./CourseTile";
import FormattedInput from "./FormattedInput";

interface Params {
    searcher: (code: string) => Promise<Course[]>;
    resolveCourse: (adddedCoruse: Course) => Promise<void>;
    shouldDisplay: boolean;
    setDisplay: (set: boolean) => void;
    heading?: string;
    defaultResults?: Course[];
}

const CourseSearch: React.FC<Params> = (props) => {

    const query = useRef<string>("");

    const [results, setResults] = useState<Course[] | undefined>(props.defaultResults);
    const [error, setError] = useState<string | undefined>(undefined);
    const [loading, setLoading] = useState<boolean>(false);

    const displayResults = (): JSX.Element => {
        const resultCards: JSX.Element[] =
            results!.map((c: Course, index) =>
                <CourseTile course={c} infoButton={{func: () => props.resolveCourse(c)}} key={index} />);
        return <Grid.Row>
            <Grid.Column>
                <Card.Group content={resultCards} />
            </Grid.Column>
        </Grid.Row>
    }

    useEffect(() => {
        if (loading) {
            setResults(undefined);
            console.log("searching");
            props.searcher(query.current)
                .then((courses) => setResults(courses))
                .catch(() => setError('"' + query.current + '" could not be found'));
            console.log("set");
            setLoading(false);
        }
    }, [loading])

    return (
        <Modal
            closeIcon
            open={props.shouldDisplay}
            onClose={() => {
                setResults(undefined)
                setLoading(false);
                props.setDisplay(false);}}>
            <Modal.Header>
                <Header
                    content={props.heading ?? "Find a course"} />
            </Modal.Header>

            <Modal.Content>
                <Grid>
                    <Grid.Row textAlign="left">
                        <Grid.Column>
                            <Header as="h3" content={"Input a valid course code (no abbreviations)"} />
                        </Grid.Column>
                    </Grid.Row>
                    <Grid.Row  verticalAlign="middle">
                        <Grid.Column width={14}>
                            <FormattedInput
                                label="search"
                                type="search"
                                textChange={(newStr: string) => {query.current = newStr; console.log(newStr);}}
                                error={(error) ? { messages: [error!], resolve: () => setError(undefined) } : undefined} />
                        </Grid.Column>
                        <Grid.Column width={2}>
                            <Button loading={loading} disabled={loading}
                                circular icon="search"
                                className="gradient"
                                onClick={() => setLoading(true)} />
                        </Grid.Column>
                    </Grid.Row>
                    {(results) ? displayResults() : undefined}
                </Grid>

            </Modal.Content>
        </Modal>
    );
}

export default CourseSearch;