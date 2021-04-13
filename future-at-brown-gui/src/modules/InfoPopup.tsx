import React from "react";
import { Icon, Popup } from "semantic-ui-react";

interface Props {
    message?: string
}

const InfoPopup: React.FC<Props> = (props) =>
    (props.message) ? <Popup
        content={props.message}
        trigger={<Icon name="question circle" color="grey"/>}
    /> : null

export default InfoPopup;