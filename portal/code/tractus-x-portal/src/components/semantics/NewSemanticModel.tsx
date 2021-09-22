// Copyright (c) 2021 T-Systems
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import { PrimaryButton, TextField } from "@fluentui/react";
import { useState } from "react";
import { addModel } from "./data";

export function NewSemanticModel(props) {
  const buttonStyle = {alignSelf: 'flex-end'};
  const [value, setValue] = useState<string | any>('');
  const [error, setError] = useState<Error | any>();

  const onInputChange =(_, input) =>{
    setValue(input);
  }

  const uploadModel = () => {
    addModel({model: value, private: true, type: 'BAMM'})
      .then(data => {
        console.log(data)
      }, error => setError(error.message));
  }

  return (
    <div className='df fdc jcc p44'>
      <h1 className="fs20 bold mb20">Add new model</h1>
      <TextField label="Paste your model into the text field." value={value} onChange={onInputChange} multiline autoAdjustHeight className="mb20" />
      <PrimaryButton style={buttonStyle} onClick={uploadModel} text="Upload model" className="asfe"/>
    </div>
  );
}
