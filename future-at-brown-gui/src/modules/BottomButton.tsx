import { Link } from "react-router-dom"
import { Button, Icon, SemanticCOLORS, SemanticICONS } from "semantic-ui-react"

//Module which creates the main buttons for our website.
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

//Component which represents our home button.
export const HomeButton: React.FC<{}> = ( ) => {
    return makeButton("home", "Home", "left", "", "blue");
}

//Component which represents our search button.
export const SearchButton: React.FC<{}> = ( ) => {
    return makeButton("search", "Search", "left", "search", "green");
}

//Component which represents our profile button.
export const ProfileButton: React.FC<{}> = ( ) => {
    return makeButton("user", "Profile", "right", "profile", "teal");
}

//Component which serves as the footer to hold our bottom row of buttons.
export const ButtonFooter: React.FC<{}> = ( ) => {
    return <div style={{height: '15vh'}} />
}