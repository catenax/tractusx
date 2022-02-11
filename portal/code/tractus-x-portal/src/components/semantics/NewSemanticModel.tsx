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

import { Dropdown, IDropdownOption, IDropdownStyles, Checkbox, PrimaryButton, TextField} from "@fluentui/react";
import { useState } from "react";
import { useHistory } from "react-router-dom";
import { addModel, encodeID, Status } from "./data";

export function NewSemanticModel(props) {
  const buttonStyle = {alignSelf: 'flex-end'};
  const buttonStyle2 = { width:200, marginLeft:10};
  const [value, setValue] = useState<string | any>('');
  const [status, setStatus] = useState<Status | any>(Status.Draft)
  const [error, setError] = useState<Error | any>(null);
  const history = useHistory();

  const onInputChange =(_, input) =>{
    setValue(input);
    setError('');
  }

  const onStatusDropdownChange = (_, option) => {
    setStatus(Status[option.key]);
  }

  const uploadModel = (create: boolean) => {
    addModel({model: value, type: 'BAMM', status: status},create)
      .then(data => {
        history.push(`/home/semanticmodel/${encodeID(data.urn)}`);
      }).catch(errorResponse => {
        let status=errorResponse.status;
        errorResponse.text().then((body) => {
            //Here is already the payload from API
            setError(`Server responds with ${status} error! ${body}`);
        });
    });
  }

  const createModel = () => { uploadModel(true); };
  const modifyModel = () => { uploadModel(false); };
  
  const availableOptions: IDropdownOption[] = Object.keys(Status).map(key => (
    { key: key, text: Status[key]}
  ));
  
  const defaultOption=availableOptions.find( option => option.text==status).key;

  const dropdownStyles: Partial<IDropdownStyles> = {
    dropdown: { width: 150, marginRight: 20 },
  };

  return (
    <div className='df fdc jcc p44'>
      <h1 className="fs20 bold mb20">Create or Modify a Model</h1>
      <Dropdown defaultSelectedKey={defaultOption} placeholder="Status" label="Status" options={availableOptions} styles={dropdownStyles} onChange={onStatusDropdownChange}/>
      <TextField label="Paste your model definition into the text field." value={value} errorMessage={error} onChange={onInputChange} multiline autoAdjustHeight className="mb20" />
      <div style={buttonStyle}>
      <PrimaryButton style={buttonStyle2} onClick={modifyModel} text="Modify model" className="asfe"/>
      <PrimaryButton style={buttonStyle2} onClick={createModel} text="Upload model" className="asfe"/>
      </div>
    </div>
  );
}
