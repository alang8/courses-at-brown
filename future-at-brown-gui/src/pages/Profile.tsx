import React from "react"
import { Header } from "semantic-ui-react"
import { User } from "../modules/Data"

interface Params {
    user: User;
}

const Profile: React.FC<Params> = (props) => {
    return <div>
        <Header as="h1" content={props.user.username} />
        <Header as="h2" content={"Profile"} />
    </div>
}

export default Profile;