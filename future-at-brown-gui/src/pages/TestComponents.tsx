import { useState } from "react";
import { Link } from "react-router-dom";
import { Button, Card, Header } from "semantic-ui-react";
import { FindCourse } from "../classes/Course";
import User from "../classes/User";
import { ButtonFooter, ProfileButton, SearchButton } from "../modules/BottomButton";
import CourseSearch from "../modules/CourseSearch";
import CourseTile from "../modules/CourseTile";
import ExpandableCourses from "../modules/ExpandableCourses";
import FormattedInput from "../modules/FormattedInput";
import InfoPopup from "../modules/InfoPopup";
import ParamSlider from "../modules/ParamSliders";

/**
 * Component which we use to test all of our individual components/style before integrating into our actual website
 */
const TestComponent: React.FC<{}> = () => {

  const [error, setError] = useState<Array<String>>(["Uhoh", "more meesages"]);
  const [dispSearch, setDispSearch] = useState<boolean>(false);


  return <div className="App">
    <SearchButton />
    <ProfileButton />
    <Header as="h1" className="logo">Future @ Brown </Header><InfoPopup message={"test"} />
    <Link to="/test-route"><Button content="test route" className="gradient" /></Link>
    <Button content={"Show search"} onClick={() => setDispSearch(true)} />
    <CourseSearch shouldDisplay={dispSearch} setDisplay={setDispSearch} resolveButton={{func: (c) => {
      console.log("Added", c);
      return Promise.resolve();
    }}}
      searcher={(inp: string) => {
        console.log(inp);
        return (inp.indexOf("t") !== -1) ? Promise.resolve([{ code: "0320", dept: "CSCI", name: "Introduction to Software Engineering" }]) : Promise.reject()
      }} />

    <FormattedInput label="Texte" />
    <FormattedInput label="Username" type="username" />
    <FormattedInput label="Password" type="password" error={{ messages: error, resolve: () => setError([]) }} />
    <ExpandableCourses modify={{searcher: FindCourse}} courses={[{ code: "0320", dept: "CSCI", name: "Introduction to Software Engineering" }]} title="Test" />
    <ExpandableCourses courses={[{ code: "0320", dept: "CSCI", name: "Introduction to Software Engineering" }, { code: "0320", dept: "CSCI", name: "Introduction to Software Engineering" }, { code: "0320", dept: "CSCI", name: "Introduction to Software Engineering" }, { code: "0320", dept: "CSCI", name: "Introduction to Software Engineering" }]} title="Test" />
    <ExpandableCourses modify={{searcher: FindCourse}} courses={[{ code: "0220", dept: "CSCI", name: "Introduction to Software Engineering" }, { code: "0320", dept: "CSCI", name: "Introduction to Software Engineering" }, { code: "0321", dept: "1SCI", name: "Introduction to Software Engineering" }, { code: "1320", dept: "CSCI", name: "Introduction to Software Engineering" }, { code: "0325", dept: "C4CI", name: "Introduction to Software Engineering" }, { code: "0324", dept: "CS3I", name: "Introduction to Software Engineering" }, { code: "2320", dept: "CSCI", name: "Introduction to Software Engineering" }, { code: "0323", dept: "CSCI", name: "Introduction to Software Engineering" }]} title="Test" />
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
