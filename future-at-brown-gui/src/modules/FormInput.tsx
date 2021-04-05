import React, { useState } from "react";
import { Form, Input, Label } from 'semantic-ui-react';

export interface Error {
    messages: Array<String>;
    resolve: () => void;
}

interface Props {
    label: string;
    textChange?: (val: string) => void;
    type?: "text" | "password" | "username";
    id?: string;
    error?: Error;
}

const FormInput: React.FC<Props> = (props) => {

    const [selected, setSelected] = useState<boolean | undefined | null>(false);

    const inError: boolean = props.error !== undefined && props.error!.messages.length > 0;

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        console.log(props.error);
        props.error?.resolve();
        if (props.textChange) {
            console.log(event.target.value);
            props.textChange!(event.target.value);
        }
    }

    const getError = (): JSX.Element | null => {
        if (inError) {
            return (
                <Label pointing='below' color='red' basic size="big">
                    <ul>
                        {props.error!.messages.map((message, index) =>
                            <li key={index}>{message}</li>
                        )}
                    </ul>
                </Label>

            );
        }

        return null;
    }

    const getColor = (): string => {
        if (inError) {
            return 'underline error';
        } else if (selected) {
            return 'underline selected';
        } else {
            return 'underline unselected';
        }
    }

    const getIcon = (): string | undefined => {
        switch (props.type) {
            case "password": return "lock";
            case "username": return "user";
            default: return undefined;
        }
    }

    console.log("errors", inError, props.error);
    return (
        <Form.Field className={"input-box"} id={props.id}>
            {getError()}
            <Input
                fluid
                transparent
                icon={getIcon()}
                iconPosition={getIcon() ? 'left' : undefined}
                type={props.type ?? "text"}
                onFocus={() => setSelected(true)}
                onBlur={() => setSelected(false)}
                placeholder={props.label + '...'}
                onChange={handleChange}
                error={inError}
            />
        
            <div className={getColor()}>
                <label>{props.label}</label>
            </div>
        </Form.Field>
    );
}

export default FormInput;

