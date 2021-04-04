import { useState } from 'react';
import './css/App.css';
import { Button, Card, Header } from 'semantic-ui-react';
import FormInput from './FormInput'
import CourseTile from './CourseTile';

function App() {

  const [text, setText] = useState<string>("test");
  const errorSet = (inp: string) => {
    console.log("set", inp);
    setText(inp)};

  return (
    <div className="App">
      <Header as="h1" className="logo">Future @ Brown</Header>
      <Button content="test" />
      <FormInput label="Texte"/>
      <FormInput label="Username"  type="username"/>
      <FormInput label="Password" type="password" error={["You fucked up", "more meesages"]} textChange={errorSet}/>
      <Header as="h1">{text}</Header>
      <Card.Group>
      <CourseTile code="0320" department="APMA" title="Introduction to Software Engineering"/>
      <CourseTile code="0320" department="CSCI" title="Introduction to Software Engineering"/>
      <CourseTile code="0320" department="CSCI" title="Introduction to Software Engineering"/>
      <CourseTile code="0320" department="ECON" title="Introduction to Software Engineering"/>
      </Card.Group>
      
    </div>
  );
}

export default App;
