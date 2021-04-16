import { useState } from "react";
import { Form, Input, Label, SemanticWIDTHS } from 'semantic-ui-react';
import 'semantic-ui-css/package.json'

export interface Error {
    messages: Array<String>;
    resolve: () => void;
}

interface Props {
    label: string;
    textChange?: (val: string) => void;
    type?: "text" | "password" | "username" | "search";
    id?: string;
    error?: Error;
    width?: SemanticWIDTHS;
    isLoading?: boolean;
}

const FormattedInput: React.FC<Props> = (props) => {

    const [selected, setSelected] = useState<boolean | undefined | null>(false);

    const inError: boolean = props.error !== undefined && props.error!.messages.length > 0;

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        props.error?.resolve();
        if (props.textChange) props.textChange!(event.target.value);
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
            case "search": return "search";
            case "password": return "lock";
            case "username": return "user";
            default: return undefined;
        }
    }

    return (
        <Form.Field className={"input-box"} id={props.id} width={props.width}>
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
                loading={props.isLoading}
                disabled={props.isLoading}
            />
        
            <div className={getColor()}>
                <label>{props.label}</label>
            </div>
        </Form.Field>
    );
}

export default FormattedInput;

