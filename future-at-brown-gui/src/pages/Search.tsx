import React from "react"
import { Header } from "semantic-ui-react"
import { User } from "../modules/Data"

interface Params {
    user: User;
}

const Search: React.FC<Params> = (props) => {
    return <div>
        <Header as="h1" content={props.user.username} />
        <Header as="h2" content={"Search"} />
    </div>
}

export default Search;