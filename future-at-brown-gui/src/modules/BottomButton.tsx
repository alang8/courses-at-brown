import React from "react"
import { Link } from "react-router-dom"
import { Button, Icon, SemanticCOLORS, SemanticICONS } from "semantic-ui-react"

const makeButton = (icon: SemanticICONS, name: string,
     justify: "left"|"right", link: string, color?: SemanticCOLORS): JSX.Element => {
        return <div className={"bottom-float " + justify} >
        <Link to={'/' + link} >
            <Button animated circular color={color}>
                <Button.Content visible>
                    <Icon name={icon} />
                </Button.Content>
                <Button.Content hidden>
                    {name}
            </Button.Content>
            </Button>
        </Link>
    </div>
}

export const HomeButton: React.FC<{}> = ( ) => {
    return makeButton("home", "Home", "left", "", "blue");
}

export const SearchButton: React.FC<{}> = ( ) => {
    return makeButton("search", "Search", "left", "search", "green");
}

export const ProfileButton: React.FC<{}> = ( ) => {
    return makeButton("user", "Profile", "right", "profile", "teal");
}

export const ButtonFooter: React.FC<{}> = ( ) => {
    return <div style={{height: '15vh'}} />
}