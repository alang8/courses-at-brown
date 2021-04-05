import React from "react";
import { Header } from "semantic-ui-react";

interface Params{
    setLogin: (user: String) => void;
}

const Login: React.FC<Params> = (props) => {
    return <Header as="h1" content="NO" />
}

export default Login;