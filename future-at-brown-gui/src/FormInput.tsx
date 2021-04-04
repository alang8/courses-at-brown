import classes from "*.module.sass";
import React, { useState } from "react";
import { Form, Input, Message, Icon } from 'semantic-ui-react';

interface Props {
    label: string;
    textChange?: (val: string) => void;
    type?: "text" | "password" | "username";
    id?: string;
    error?: Array<String>;
}

const FormInput: React.FC<Props> = (props) => {

    const [selected, setSelected] = useState<boolean | undefined | null>(false);
    const [error, setError] = useState<Array<String> | undefined>(props.error);

    const notInError: boolean = error !== undefined && error !== [];

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setError(undefined);
        if (props.textChange) {
            console.log(event.target.value);
            props.textChange!(event.target.value);
        }
    }

    const getError = (): JSX.Element | null => {
        if (notInError) {
            return (
                <Message icon compact color="red" size="tiny">
                    <Icon name="warning sign" />
                    <Message.Content>
                        <Message.Header>{'Invalid ' + props.label}</Message.Header>
                        <Message.List>
                            {error!.map((message, index) =>
                                <Message.Item key={index}>{message}</Message.Item>
                            )}
                        </Message.List>
                    </Message.Content>
                </Message>
            );
        }

        return null;
    }

    const getColor = (): string => {
        if (notInError) {
            return 'underline error';
        } else if (selected) {
            return 'underline selected';
        } else {
            return 'underline unselected';
        }
    }

    const getIcon = (): string | undefined => {
        switch(props.type) {
            case "password": return "lock";
            case "username": return "user";
            default: return undefined;
        }
    }

    return (
        <Form.Field className={"input-box"} id={props.id}>
            {getError()}
            <Input
                fluid
                transparent
                icon = {getIcon()}
                iconPosition={getIcon()?'left':undefined}
                type={props.type ?? "text"}
                onFocus={() => setSelected(true)}
                onBlur={() => setSelected(false)}
                placeholder={props.label + '...'} 
                onChange={handleChange}
                error={notInError}
                />
            <div className={getColor()}>
                <label>{props.label}</label>
            </div>
        </Form.Field>
    );
}

export default FormInput;

