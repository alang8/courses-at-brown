import React, { useEffect, useState } from 'react';
import {
  BrowserRouter as Router,
  Route,
  Switch,
  Redirect
} from "react-router-dom";
import './css/App.css';
import SplashPage from './pages/SplashPage';
import Login from './pages/Login';
import Signup from './pages/Signup';
import Search from './pages/Search';
import Profile from './pages/Profile';
import GraphDisplay from "./pages/GraphDisplay";
import TestComponent, { TestComponent2 } from './pages/TestComponents';
import NotFound from './pages/NotFound';
import User, { destringify } from './classes/User';

const App: React.FC<{}> = () => {
  const [user, setUser] = useState<User | undefined>(destringify(localStorage.getItem("user")));
  console.log("in app.tsx");
  console.log(user);

  // routes to be used if the user is not logged in (profile page otherwise)
  const InauthenticatedRoute = (route: string, loginProcess: JSX.Element): JSX.Element => {
    console.log("inauth check", user);
    return <Route exact path={route}>
      {(user) ? <Redirect to="/profile" /> : loginProcess}
    </Route>
  }

  // routes to be used if the user is logged in (splash page otherwise)
  const AuthenticatedRoute = (route: string, protectedContent: JSX.Element): JSX.Element => {
    console.log("auth check", user);
    return <Route exact path={route}>
      {(user) ? protectedContent : <Redirect to="/splash" />}
    </Route>;
  }

  useEffect(() => {
    if (user) {
      localStorage.setItem("user", user.stringify());
    }
  }, [user]);

  return (
    <Router>
      <Switch>
        <Route path="/test-components" component={TestComponent} />
        <Route path="/test-route" component={TestComponent2} />
        {InauthenticatedRoute("/", <Redirect to="/splash" />)}
        {AuthenticatedRoute("/search", <Search user={user!} setUser={setUser}/>)}
        {AuthenticatedRoute("/profile", <Profile user={user!} setUser={setUser}/>)}
        {InauthenticatedRoute("/splash", <SplashPage setLogin={setUser} />)}
        {AuthenticatedRoute("/graph", <GraphDisplay user={user!} setUser={setUser}/>)}
        {InauthenticatedRoute("/login", <Login setLogin={setUser} />)}
        {InauthenticatedRoute("/signup", <Signup setLogin={setUser} />)}
        <Route path="*" component={NotFound} />
      </Switch>
    </Router>
  )
}

export default App;