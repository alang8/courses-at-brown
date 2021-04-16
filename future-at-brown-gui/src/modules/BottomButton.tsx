import React from "react"
import { Link } from "react-router-dom"
import { Button, Icon, SemanticCOLORS, SemanticICONS } from "semantic-ui-react"

//Module which creates the main buttons for our website.
const makeButton = (icon: SemanticICONS, name: string,
                    justify: "left" | "right" | "center", link?: string,
                    color?: SemanticCOLORS, func?: () => any, disabled?: boolean): JSX.Element => {

    const button = (
        <Button inverted animated circular color={color} onClick={func} disabled={disabled}>
            <Button.Content visible>
                <Icon name={icon} />
            </Button.Content>
            <Button.Content hidden>
                {name}
            </Button.Content>
        </Button>
    )
    return <div className={"bottom-float " + justify} >
        {(link) ? <Link to={'/' + link} children={button} />
            : button}
    </div>
}

//Component which represents our home button.
export const HomeButton: React.FC<{}> = () => {
    return makeButton("home", "Home", "left", "", "blue");
}

//Component which represents our search button.
export const SearchButton: React.FC<{}> = () => {
    return makeButton("search", "Search", "left", "search", "green");
}

//Component which represents our profile button.
export const ProfileButton: React.FC<{}> = () => {
    return makeButton("user", "Profile", "right", "profile", "teal");
}

export const GraphButton: React.FC<{ disabled?: boolean, justify: "left" | "right" }> =
    (props) => {
        return makeButton("sitemap", "Graph", props.justify, "graph", "blue", undefined,
            props.disabled);
    }

interface Params {
    icon: SemanticICONS;
    text: string;
    color?: SemanticCOLORS;
    oreintation?: "left" | "right" | "center";
    func?: () => any;
}

export const CustomButton: React.FC<Params> = (props) => {
    return makeButton(props.icon, props.text, props.oreintation ?? "center",
        undefined, props.color, props.func);
}
//Component which serves as the footer to hold our bottom row of buttons.
export const ButtonFooter: React.FC<{}> = () => {
    return <div style={{ height: '15vh' }} />
}