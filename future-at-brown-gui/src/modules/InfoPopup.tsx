import React from "react";
import { Icon, Popup } from "semantic-ui-react";

interface Props {
    message?: React.ReactNode
}

//Module which represents the an information popup to describe how our site works.
const InfoPopup: React.FC<Props> = (props) =>
    (props.message) ? <Popup
        content={props.message}
        trigger={<Icon name="question circle" color="grey"/>}
    /> : null

export default InfoPopup;