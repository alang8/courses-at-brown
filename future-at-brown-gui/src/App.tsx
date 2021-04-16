import { useEffect, useRef, useState } from 'react';
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
import User, { GetStoredUser } from './classes/User';
import { GetStoredPath, Path, StorePath } from './classes/Path';

const App: React.FC<{}> = () => {

  const [user, setUser] = useState<User | undefined>(GetStoredUser());
  const [path, setPath] = useState<Path | undefined>(GetStoredPath());
  const [redirectGraph, setRedirect] = useState<boolean>(false);

  const redirectMessage = useRef<String | undefined>(undefined);

  // routes to be used if the user is not logged in (profile page otherwise)
  const InauthenticatedRoute = (route: string, loginProcess: JSX.Element): JSX.Element => {
    return <Route exact path={route}>
      {(user) ? <Redirect to="/profile" /> : loginProcess}
    </Route>
  }

  // routes to be used if the user is logged in (splash page otherwise)
  const AuthenticatedRoute = (route: string, protectedContent: JSX.Element): JSX.Element => {
    return <Route exact path={route}>
      {(user) ? protectedContent : <Redirect to="/splash" />}
    </Route>;
  }

  // redirect only if path changes to a defined value
  useEffect(() => {
    if (path) setRedirect(true);
  }, [path]);

  console.log("path", path);
  return (
    <Router>
      <Switch>
        <Route path="/test-components" component={TestComponent} />
        <Route path="/test-route" component={TestComponent2} />
        {redirectGraph ? <Route path="/search"><Redirect to="/graph" /></Route> : undefined}
        {InauthenticatedRoute("/", <Redirect to="/splash" />)}
        {AuthenticatedRoute("/search", <Search user={user!}
          setUser={setUser}
          setPath={StorePath(setPath)}
          hasGraph={path ? false : true} />)}
        {AuthenticatedRoute("/profile", <Profile user={user!} setUser={setUser} />)}
        {InauthenticatedRoute("/splash", <SplashPage setLogin={setUser} />)}
        {AuthenticatedRoute("/graph", <GraphDisplay user={user!} setUser={setUser} path={path} onRender={() => setRedirect(false)} />)}
        {InauthenticatedRoute("/login", <Login setLogin={setUser} />)}
        {InauthenticatedRoute("/signup", <Signup setLogin={setUser} />)}
        <Route path="*" component={NotFound} />
      </Switch>
    </Router>
  )
}

export default App;