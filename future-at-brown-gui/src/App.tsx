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
import FormattedInput from './modules/FormattedInput'
import CourseTile from './modules/CourseTile';
import SplashPage from './pages/SplashPage';
import Login from './pages/Login';
import Signup from './pages/Signup';
import Search from './pages/Search';
import Profile from './pages/Profile';
import { getUser, User } from './modules/Data';
import ExpandableCourses from './modules/ExpandableCourses';
import GraphDisplay from "./pages/GraphDisplay";

const TestComponent: React.FC<{}> = () => {

  const [error, setError] = useState<Array<String>>(["Uhoh", "more meesages"]);

  return <div className="App">
    <Header as="h1" className="logo">Future @ Brown</Header>
    <Link to="/test-route"><Button content="test" className="gradient"/></Link>
    <FormattedInput label="Texte" />
    <FormattedInput label="Username" type="username" />
    <FormattedInput label="Password" type="password" error={{ messages: error, resolve: () => setError([]) }} />

    <ExpandableCourses courses={[{code: "0320", dept: "CSCI", name: "Introduction to Software Engineering"}]} title="Test" />
    {/* <ExpandableCourses courses={[{code: "0320", dept: "CSCI", name: "Introduction to Software Engineering"}, {code: "0320", dept: "CSCI", name: "Introduction to Software Engineering"}, {code: "0320", dept: "CSCI", name: "Introduction to Software Engineering"}, {code: "0320", dept: "CSCI", name: "Introduction to Software Engineering"}]} title="Test" />
    <ExpandableCourses courses={[{code: "0320", dept: "CSCI", name: "Introduction to Software Engineering"}, {code: "0320", dept: "CSCI", name: "Introduction to Software Engineering"}, {code: "0320", dept: "CSCI", name: "Introduction to Software Engineering"}, {code: "0320", dept: "CSCI", name: "Introduction to Software Engineering"}, {code: "0320", dept: "CSCI", name: "Introduction to Software Engineering"}, {code: "0320", dept: "CSCI", name: "Introduction to Software Engineering"}, {code: "0320", dept: "CSCI", name: "Introduction to Software Engineering"}, {code: "0320", dept: "CSCI", name: "Introduction to Software Engineering"}]} title="Test" /> */}
    <Card.Group>
      <CourseTile course={{code: "0320", dept: "APMA", name: "Introduction to Software Engineering"}} />
      <CourseTile course={{code: "0320", dept: "CSCI", name: "Introduction to Software Engineering"}} />
      <CourseTile course={{code: "0320", dept: "1ECON", name: "Introduction to Software Engineering"}} />
      <CourseTile course={{code: "0320", dept: "E2CON", name: "Introduction to Software Engineering"}} />
      <CourseTile course={{code: "0320", dept: "ECreO3N", name: "Introduction to Software Engineering"}} />
      <CourseTile course={{code: "0320", dept: "EC42ON", name: "Introduction to Software Engineering"}} />
      <CourseTile course={{code: "0320", dept: "EqCO3N", name: "Introduction to Software Engineering"}} />
      <CourseTile course={{code: "0320", dept: "EwCON", name: "Introduction to Software Engineering"}} />
      <CourseTile course={{code: "0320", dept: "ECeON", name: "Introduction to Software Engineering"}} />
      <CourseTile course={{code: "0320", dept: "ECtrON", name: "Introduction to Software Engineering"}} />
      <CourseTile course={{code: "0320", dept: "ECyON", name: "Introduction to Software Engineering"}} />
      <CourseTile course={{code: "0320", dept: "ECuON", name: "Introduction to Software Engineering"}} />
      <CourseTile course={{code: "0320", dept: "ECgON", name: "Introduction to Software Engineering"}} />
      <CourseTile course={{code: "0320", dept: "EChON", name: "Introduction to Software Engineering"}} />
      <CourseTile course={{code: "0320", dept: "EChON", name: "g"}} />
    </Card.Group>
  </div>
}

const TestComponent2: React.FC<void> = () => (
  <Header as="h1"> Page 2 <Link to="/test-components"><Button content="let me out" /></Link></Header>
);

const NotFound: React.FC<void> = () => (
  <Container className="total-page">
    <Icon name='question' size='massive' />
    <Header as="h1">Error 404</Header>
    <Header as="h3">The page you requested does not exist</Header>
    <Header as="h2"><Link to="/"><Button content="Return Home" /></Link></Header>
  </Container>
)

function App() {
  const [user, setUser] = useState<User | undefined>(undefined);

  const setUserByName = async (user: string) => setUser(await getUser(user));
  const setNewUser = (user: User) => {
    setUser(user);
    // do something with the database
  }

  // routes to be used if the user is not logged in (profile page otherwise)
  const InauthenticatedRoute = (route: string, loginProcess: JSX.Element): JSX.Element =>
    <Route exact path={route}>
      {(user) ? <Redirect to="/profile" /> : loginProcess}
    </Route>

  // routes to be used if the user is logged in (splash page otherwise)
  const AuthenticatedRoute = (route: string, protectedContent: JSX.Element): JSX.Element =>
    <Route exact path={route}>
      {(user) ? protectedContent : <Redirect to="/splash" />}
    </Route>

  return (
    <Router>
      <Switch>
        <Route path="/test-components" component={TestComponent} />
        <Route path="/test-route" component={TestComponent2} />
        {InauthenticatedRoute("/", <Redirect to ="/splash" />)}
        {AuthenticatedRoute("/search", <Search user={user!} />)}
        {AuthenticatedRoute("/profile", <Profile user={user!} />)}
        {InauthenticatedRoute("/splash", <SplashPage setLogin={setUser}/>)}
        {InauthenticatedRoute("/graph", <GraphDisplay user={user!} />)}
        {InauthenticatedRoute("/login", <Login setLogin={setUserByName} />)}
        {InauthenticatedRoute("/signup", <Signup setLogin={setNewUser} />)}
        <Route path="*" component={NotFound} />
      </Switch>
    </Router>
  )
}

export default App;