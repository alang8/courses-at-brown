import React, { useState } from 'react';
import {
  BrowserRouter as Router,
  Route,
  Switch,
  Link
} from "react-router-dom";
import './css/App.css';
import { Button, Card, Container, Header, Icon } from 'semantic-ui-react';
import FormInput from './FormInput'
import CourseTile from './CourseTile';

const testComponent: React.FC<void> = () => (
  <div className="App">
    
    <Header as="h1" className="logo">Future @ Brown</Header>
    <Link to="/test"><Button content="test" /></Link>
    <FormInput label="Texte" />
    <FormInput label="Username" type="username" />
    <FormInput label="Password" type="password" error={["You fucked up", "more meesages"]} />
    <Card.Group>
      <CourseTile code="0320" department="APMA" title="Introduction to Software Engineering" />
      <CourseTile code="0320" department="CSCI" title="Introduction to Software Engineering" />
      <CourseTile code="0320" department="1ECON" title="Introduction to Software Engineering" />
      <CourseTile code="0320" department="E2CON" title="Introduction to Software Engineering" />
      <CourseTile code="0320" department="ECreO3N" title="Introduction to Software Engineering" />
      <CourseTile code="0320" department="EC42ON" title="Introduction to Software Engineering" />
      <CourseTile code="0320" department="EqCO3N" title="Introduction to Software Engineering" />
      <CourseTile code="0320" department="EwCON" title="Introduction to Software Engineering" />
      <CourseTile code="0320" department="ECeON" title="Introduction to Software Engineering" />
      <CourseTile code="0320" department="ECtrON" title="Introduction to Software Engineering" />
      <CourseTile code="0320" department="ECyON" title="Introduction to Software Engineering" />
      <CourseTile code="0320" department="ECuON" title="Introduction to Software Engineering" />
      <CourseTile code="0320" department="ECgON" title="Introduction to Software Engineering" />
      <CourseTile code="0320" department="EChON" title="Introduction to Software Engineering" />
      <CourseTile code="0320" department="EChON" title="Introduction to Software Engineering" />
    </Card.Group>

  </div>
);

const testComponent2: React.FC<void> = () => (
  <Header as="h1"> Page 2 <Link to="/"><Button content="let me out" /></Link></Header>
);

const notFound: React.FC<void> = () => (
  <Container className="not-found">
    <Icon name='question' size='massive' />
    <Header as="h1">Error 404</Header>
    <Header as="h3">The page you requested does not exist</Header>
    <Header as="h2"><Link to="/"><Button content="Return Home" /></Link></Header>
  </Container>
)

function App() {
  return (
    <Router>
      <Switch>
      <Route path="/test" component={testComponent2} />
      <Route exact path="/" component={testComponent} />
      <Route path="*" component={notFound} />
      </Switch>
    </Router>
  )
}

export default App;
