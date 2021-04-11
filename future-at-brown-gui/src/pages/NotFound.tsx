import React from "react";
import { Link } from "react-router-dom";
import { Container, Icon, Header, Button } from "semantic-ui-react";

const NotFound: React.FC<void> = () => (
    <Container className="total-page">
      <Icon name='question' size='massive' />
      <Header as="h1" content={"Error 404"} />
      <Header as="h3" content={"The page you requested does not exis"} />
      <Header as="h2"><Link to="/"><Button content="Return Home" /></Link></Header>
    </Container>
  )

  export default NotFound;