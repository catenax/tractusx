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
import { observer } from 'mobx-react';
import * as vis from 'vis-network';
import UserService from '../helpers/UserService';

@observer
export default class ConnectorOverview extends React.Component {
  private network: any;

  componentDidMount() {
    const company = UserService.getDomain() || 'xAMPLcorp';
    const margin = {
      top:20,
      right:20,
      bottom:20,
      left:20
    }
    const nodes = [
      { id: 1, label: `${company}\nConnector`, x: 0, y: 0, color: '#E20074', margin: margin},
      { id: 2, label: 'Digital Twin Registry\nIDS Broker', color: '#969696', margin: margin, shape: "ellipse" },
      { id: 3, label: 'Semantic Hub\nVocabulary Provider', color: '#969696', margin: margin, shape: "ellipse" },
      { id: 4, label: 'Catena-X Core Services\nConnector', color: 'rgb(255,188,64)', margin: margin},
      { id: 5, label: 'CO2 Footprint\nConnector', color: 'rgb(79,203,45)', margin: margin},
      { id: 6, label: 'Circular Economy\nConnector', color: 'rgb(79,203,45)', margin: margin},
      { id: 7, label: 'SAP Material Traceability\nConnector', color: '#1661BE', margin: margin},
      { id: 8, label: 'ZF\nConnector',  color: 'rgb(17,121,191)', margin: margin},
      { id: 9, label: 'Bosch\nConnector', color: '#E00420', margin: margin }
    ];

    // create an array with edges
    const edges = [
      { from: 4, to: 1, arrows: "to, from" },
      { from: 5, to: 1, arrows: "to, from" },
      { from: 6, to: 1, arrows: "to, from" },
      { from: 7, to: 1, arrows: "to, from" },
      { from: 8, to: 1, arrows: "to, from" },
      { from: 9, to: 1, arrows: "to, from" },
      { from: 2, to: 1, dashes:true },
      { from: 3, to: 1, dashes:true }
    ];

    // create a network
    const container = document.getElementById('mynetwork');
    const data = { nodes: nodes, edges: edges };

    const options = {
      autoResize: true,
      height: '100%',
      width: '100%',
      nodes: {
        shape: 'box',
        font: { color: '#ffffff' }
      },
      edges: {
        color: {
          color: '#004893',
          highlight: '#E49106'
        },
        smooth: {
          enabled: true,
          forceDirection: 'horizontal',
          roundness: 0,
          type: 'cubicBezier'
        },
        length:250
      }
    };

    this.network = new vis.Network(container, data, options);
  }

  public render() {
    return (
      <div className='w100pc pt18 h100pc df fdc'>
        <div id='mynetwork' className='w100pc h600'/>
      </div>
    );
  }
}