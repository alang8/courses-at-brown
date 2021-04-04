import React, { useState } from 'react';
import {
  BrowserRouter as Router,
  Route,
  Switch,
  Link,
  Redirect
} from "react-router-dom";
import './css/App.css';
import { Button, Card, Container, Header, Icon } from 'semantic-ui-react';
import FormInput from './FormInput'
import CourseTile from './CourseTile';
import SplashPage from './SplashPage';

const testComponent: React.FC<void> = () => (
  <div className="App">

    <Header as="h1" className="logo">Future @ Brown</Header>
    <Link to="/test-route"><Button content="test" /></Link>
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
      <CourseTile code="0320" department="EChON" title="g" />
    </Card.Group>

  </div>
);

const testComponent2: React.FC<void> = () => (
  <Header as="h1"> Page 2 <Link to="/test-components"><Button content="let me out" /></Link></Header>
);

const notFound: React.FC<void> = () => (
  <Container className="total-page">
    <Icon name='question' size='massive' />
    <Header as="h1">Error 404</Header>
    <Header as="h3">The page you requested does not exist</Header>
    <Header as="h2"><Link to="/"><Button content="Return Home" /></Link></Header>
  </Container>
)

function App() {

  const [user, setUser] = useState<String | undefined>(undefined);

  return (
    <Router>
      <Switch>
        <Route path="/test-components" component={testComponent} />
        <Route path="/test-route" component={testComponent2} />
        <Route exact path="/">
          {(user) ? <Redirect to="/profile" /> : <Redirect to="/splash" />}
        </Route>
        <Route path="/search"/>
        <Route path="/splash" component={SplashPage} />
        <Route path="*" component={notFound} />
      </Switch>
    </Router>
  )
}

export default App;
