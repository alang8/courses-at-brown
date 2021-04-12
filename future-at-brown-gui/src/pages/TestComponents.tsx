import React, { useState } from "react";
import { Link } from "react-router-dom";
import { Button, Card, Header } from "semantic-ui-react";
import User from "../classes/User";
import { ButtonFooter, ProfileButton, SearchButton } from "../modules/BottomButton";
import CourseSearch from "../modules/CourseSearch";
import CourseTile from "../modules/CourseTile";
import ExpandableCourses from "../modules/ExpandableCourses";
import FormattedInput from "../modules/FormattedInput";
import ParamSlider from "../modules/ParamSliders";

const TestComponent: React.FC<{}> = () => {

  const [error, setError] = useState<Array<String>>(["Uhoh", "more meesages"]);
  const [dispSearch, setDispSearch] = useState<boolean>(false);


  return <div className="App">
    <SearchButton />
    <ProfileButton />
    <Header as="h1" className="logo">Future @ Brown</Header>
    <Link to="/test-route"><Button content="test route" className="gradient" /></Link>
    <Button content={"Show search"} onClick={() => setDispSearch(true)} />
    <CourseSearch shouldDisplay={dispSearch} setDisplay={setDispSearch} resolveCourse={(c) => {
      console.log("Added", c);
      return Promise.resolve();
    }}
      searcher={(inp: string) => {
        console.log(inp);
        return (inp.indexOf("t") !== -1) ? Promise.resolve([{ code: "0320", dept: "CSCI", name: "Introduction to Software Engineering" }]) : Promise.reject()
      }} />

    <FormattedInput label="Texte" />
    <FormattedInput label="Username" type="username" />
    <FormattedInput label="Password" type="password" error={{ messages: error, resolve: () => setError([]) }} />
    <ExpandableCourses modifiable courses={[{ code: "0320", dept: "CSCI", name: "Introduction to Software Engineering" }]} title="Test" />
    <ExpandableCourses courses={[{ code: "0320", dept: "CSCI", name: "Introduction to Software Engineering" }, { code: "0320", dept: "CSCI", name: "Introduction to Software Engineering" }, { code: "0320", dept: "CSCI", name: "Introduction to Software Engineering" }, { code: "0320", dept: "CSCI", name: "Introduction to Software Engineering" }]} title="Test" />
    <ExpandableCourses courses={[{ code: "0320", dept: "CSCI", name: "Introduction to Software Engineering" }, { code: "0320", dept: "CSCI", name: "Introduction to Software Engineering" }, { code: "0320", dept: "CSCI", name: "Introduction to Software Engineering" }, { code: "0320", dept: "CSCI", name: "Introduction to Software Engineering" }, { code: "0320", dept: "CSCI", name: "Introduction to Software Engineering" }, { code: "0320", dept: "CSCI", name: "Introduction to Software Engineering" }, { code: "0320", dept: "CSCI", name: "Introduction to Software Engineering" }, { code: "0320", dept: "CSCI", name: "Introduction to Software Engineering" }]} title="Test" />
    <Card.Group>
      <CourseTile course={{ code: "0320", dept: "APMA", name: "Introduction to Software Engineering" }} />
      <CourseTile course={{ code: "0320", dept: "CSCI", name: "Introduction to Software Engineering" }} />
      <CourseTile course={{ code: "0320", dept: "1ECON", name: "Introduction to Software Engineering" }} />
      <CourseTile course={{ code: "0320", dept: "E2CON", name: "Introduction to Software Engineering" }} />
      <CourseTile course={{ code: "0320", dept: "ECreO3N", name: "Introduction to Software Engineering" }} />
      <CourseTile course={{ code: "0320", dept: "EC42ON", name: "Introduction to Software Engineering" }} />
      <CourseTile course={{ code: "0320", dept: "EqCO3N", name: "Introduction to Software Engineering" }} />
      <CourseTile course={{ code: "0320", dept: "EwCON", name: "Introduction to Software Engineering" }} />
      <CourseTile course={{ code: "0320", dept: "ECeON", name: "Introduction to Software Engineering" }} />
      <CourseTile course={{ code: "0320", dept: "ECtrON", name: "Introduction to Software Engineering" }} />
      <CourseTile course={{ code: "0320", dept: "ECyON", name: "Introduction to Software Engineering" }} />
      <CourseTile course={{ code: "0320", dept: "ECuON", name: "Introduction to Software Engineering" }} />
      <CourseTile course={{ code: "0320", dept: "ECgON", name: "Introduction to Software Engineering" }} />
      <CourseTile course={{ code: "0320", dept: "EChON", name: "Introduction to Software Engineering" }} />
      <CourseTile course={{ code: "0320", dept: "EChON", name: "g" }} />
    </Card.Group>
    <ParamSlider curUser={new User()} />
    <ButtonFooter />
  </div>
}

export default TestComponent;

export const TestComponent2: React.FC<void> = () => (
  <Header as="h1"> Page 2 <Link to="/test-components"><Button content="let me out" /></Link></Header>
);
