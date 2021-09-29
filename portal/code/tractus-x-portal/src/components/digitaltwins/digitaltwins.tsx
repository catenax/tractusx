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

import * as React from 'react';
import { Link } from 'react-router-dom';
import DescriptionList from '../lists/descriptionlist';
import Loading from '../loading';
import { DigitalTwin, getTwins } from './data';

const placeHolderTwins = [
  {id: '1wkjhdlwhd:wdwdlwjd:djaldj', description: 'Great description of a twin', manufacturer: 'Company A', localIdentifiers: ['', ''], aspects: ['', '']},
  {id: '2wkjhdlwhd:wdwdlwjd:djaldj', description: 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.', manufacturer: 'Company B', localIdentifiers: ['', '', ''], aspects: ['', '', '', '', '', '']},
  {id: '3wkjhdlwhd:wdwdlwjd:djaldj', description: 'At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.', manufacturer: 'Company C', localIdentifiers: [''], aspects: ['']},
  {id: '4wkjhdlwhd:wdwdlwjd:djaldj', description: 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, ...', manufacturer: 'Company D', localIdentifiers: ['', '', '', ''], aspects: ['', '']}
]

export default class DigitalTwins extends React.Component<DigitalTwin, any>{

  constructor(props) {
    super(props);
    this.state = { 
      twins: null,
      error: null
    };
  }

  componentDidMount() {
    this.setTwins();
  }

  setTwins(){
    getTwins()
      .then(
        twins => this.setState({twins: twins.items.length > 0 ? twins.items : placeHolderTwins}),
        error => this.setState({error: error.message})
      );
  }

  public render() {
    return (
      <div className='p44'>
        {this.state.twins ?
          <div>
            <h1 className="fs24 bold mb20">Digital Twins</h1>
            {this.state.twins.length === 0 ?
              <Loading /> :
              <div className="df fwrap">
                {this.state.twins.map(twin => (
                  <Link key={twin.id} className="m5 p20 bgpanel flex40 br4 bsdatacatalog tdn" to={{
                    pathname: `/home/digitaltwin/${twin.id}`
                  }}>
                    <h2 className='fs24 fg191 bold'>{twin.id}</h2>
                    <p className='fs14 fg191 pt8 mb20'>{twin.description}</p>
                    <DescriptionList title="Manufacturer:" description={twin.manufacturer}/>
                    <DescriptionList title="Aspects count:" description={twin.localIdentifiers.length}/>
                    <DescriptionList title="local Identifiers count:" description={twin.aspects.length}/>
                  </Link>
                  
                ))}
              </div>
            } 
          </div> :
        <div className="h100pc df jcc">
          {this.state.error ? <p>{this.state.error}</p> : <Loading />}
        </div>
      }
      </div>
    );
  }
}
