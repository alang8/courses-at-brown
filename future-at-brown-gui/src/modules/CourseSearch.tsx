import React, { useState, useEffect, useRef } from "react";
import { Button, Card, Grid, Header, Modal, SemanticICONS } from 'semantic-ui-react';
import { Course } from '../classes/Course'
import CourseTile from "./CourseTile";
import FormattedInput from "./FormattedInput";

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
}

const CourseSearch: React.FC<Params> = (props) => {

    const query = useRef<string>("");

    const [results, setResults] = useState<Course[] | undefined>(undefined);
    const [error, setError] = useState<string | undefined>(undefined);
    const [loading, setLoading] = useState<boolean>(false);

    const displayResults = (): JSX.Element => {
        const resultCards: JSX.Element[] =
            results!.map((c: Course, index) =>
                <CourseTile course={c} infoButton={props.resolveButton} key={index} />);
        return <Grid.Row>
            <Grid.Column>
                <Card.Group content={resultCards} />
            </Grid.Column>
        </Grid.Row>
    }

    useEffect(() => {
        if (loading) {
            setResults(undefined);
            props.searcher(query.current)
                .then((courses) => setResults(courses))
                .catch(() => setError('"' + query.current + '" could not be found'));
            setLoading(false);
        }
    }, [loading])

    useEffect(() => {
        if (props.shouldDisplay) {
            setResults(props.initialResults);
        } else {
            setResults(undefined);
            setLoading(false);
        }
    }, [props.shouldDisplay]);
    return (
        <Modal
            closeIcon
            open={props.shouldDisplay}
            onClose={() => {
                props.setDisplay(false);
                
            }}>
            <Modal.Header>
                <Grid>
                    <Grid.Row textAlign="center">
                        <Grid.Column>
                            <Header content={props.heading ?? "Find a course"} subheading={""}/>
                        </Grid.Column>
                    </Grid.Row>
                    <Grid.Row verticalAlign="middle">
                        <Grid.Column width={14}>
                            <FormattedInput
                                label="search"
                                type="search"
                                textChange={(newStr: string) => query.current = newStr}
                                error={(error) ? { messages: [error!], resolve: () => setError(undefined) } : undefined} />
                        </Grid.Column>
                        <Grid.Column width={2}>
                            <Button loading={loading} disabled={loading}
                                circular icon="search"
                                className="gradient"
                                onClick={() => setLoading(true)} />
                        </Grid.Column>
                    </Grid.Row>
                </Grid>
            </Modal.Header>

            <Modal.Content scrolling>
                {(results) ? displayResults() : undefined}
            </Modal.Content>
        </Modal >
    );
}

export default CourseSearch;