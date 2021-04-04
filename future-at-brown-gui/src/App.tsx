import React, { useState } from 'react';
import logo from './logo.svg';
import './css/App.css';
import { Button, Header } from 'semantic-ui-react';
import FormInput from './FormInput'

function App() {

  const [text, setText] = useState<string>("test");
  const errorSet = (inp: string) => {
    console.log("set", inp);
    setText(inp)};

  return (
    <div className="App">
      <Button content="test" />
      <FormInput label="Texte"/>
      <FormInput label="Username" textChange={errorSet} type="username"/>
      <FormInput label="Password" type="password" error={["You fucked up", "more meesages"]}/>
      <Header as="h1">{text}</Header>
    </div>
  );
}

export default App;
