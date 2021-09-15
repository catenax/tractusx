// Copyright (c) 2021 Microsoft
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

import * as React from 'react';
import BackLink from "./navigation/backlink";

export default class Aspect extends React.Component<any, any> {

  constructor(props) {
    super(props);
    this.state = { value: 'Accessing App Connector For Aspect Resource'};

    this.handleChange = this.handleChange.bind(this);

    let myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");
    //myHeaders.append("Authorization", "Basic YWRtaW46cGFzc3dvcmQ=");

    /*const raw = JSON.stringify([
    {
      "@type": "ids:Permission",
      "ids:action": [],
      "ids:target": "http://localhost:8080/api/artifacts/9a2e9bd4-5668-4b88-839f-9b7ea496d6fc"
    }]);*/

    const requestOptions = {
      method: 'GET',
      headers: myHeaders
      //, body: raw
    };

    console.log(requestOptions);

    const that = this;

    //const prom=fetch("http://localhost:8081/api/ids/contract?recipient=http://localhost:8080/api/ids/data&resourceIds=http://localhost:8080/api/offers/9f000a77-a189-4e24-8971-d83478217cc3&artifactIds=http://localhost:8080/api/artifacts/9a2e9bd4-5668-4b88-839f-9b7ea496d6fc&download=false", requestOptions)
    const prom = fetch("http://localhost:8082/adapter/download?file=Bremse_Windchill.xml", requestOptions)
    .then( answer => answer.text())
    .then( txt => that.setState({value:txt}))
    .catch( error => console.log(error.toString())) 

  }

  handleChange(event) {
    this.setState({value: event.target.value});
  }

  render() {
    let backlink;
    if(this.props.history !== undefined) {
      backlink=<BackLink history={this.props.history} />;
    }
    return(
      <div className='h100pc df fdc p44'>
        <div className="df jcsb w100pc">
          {backlink}
        </div>
        <p className="p24 fg1 bgindustrial fgf2">{this.state.value}</p>
      </div>
    );
  }
    
}
