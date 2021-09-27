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
import Loading from '../loading';
import { getTwins } from './data';

const placeHolderTwins = [
  {id: '1', description: 'sfssf fsfs sfsfsf sfsdfs'},
  {id: '2', description: 'sfssf fsfs sfsfsf sfsdfs'},
  {id: '3', description: 'sfssf fsfs sfsfsf sfsdfs'},
  {id: '4', description: 'sfssf fsfs sfsfsf sfsdfs'}
]

export default class DigitalTwins extends React.Component<any, any>{

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
                    <span className='fs14 fg191 pt8'>{twin.description}</span>
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
