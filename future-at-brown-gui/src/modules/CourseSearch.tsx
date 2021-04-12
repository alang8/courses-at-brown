import { geoClipAntimeridian } from "d3-geo";
import React, { useState, useEffect, useRef } from "react";
import { Button, Grid, Header, Icon, Modal } from 'semantic-ui-react';
import { GetColor } from '../classes/Colors'
import { Course } from '../classes/Course'
import FormattedInput from "./FormattedInput";

interface Params {
    searcher: (code: string) => Promise<Course>;
    setCourse: (course: Course) => void;
    shouldDisplay: boolean;
    setDisplay: (set: boolean) => void;
    heading?: string;
}

const CourseSearch: React.FC<Params> = (props) => {

    const query = useRef<string | undefined>(undefined);

    const [error, setError] = useState<string | undefined>(undefined);
    const [loading, setLoading] = useState<boolean>(false);

    useEffect(() => {
        if (loading && !query.current) {
            props.searcher(query.current!)
                .then((course) => props.setCourse(course))
                .catch(() => {
                    setError('"' + query + '" could not be found');
                    setLoading(false)
                })
        }
    }, [loading])

    return (
        <Modal
            closeIcon
            open={props.shouldDisplay}
            onClose={() => props.setDisplay(false)}>
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
                    <Grid.Row>
                        <Grid.Column width={14}>
                            <FormattedInput
                                label="search"
                                type="search"
                                textChange={(newStr: string) => query.current = newStr}
                                error={(error) ? { messages: [error!], resolve: () => setError(undefined) } : undefined} />
                        </Grid.Column>
                        <Grid.Column width={2}>
                            <Button loading={loading}
                                circular icon="search"
                                className="gradient"
                                onClick={() => setLoading(true)} />
                        </Grid.Column>
                    </Grid.Row>
                </Grid>

            </Modal.Content>
        </Modal>
    );
}

export default CourseSearch;