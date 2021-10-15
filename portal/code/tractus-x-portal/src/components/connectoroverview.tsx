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
import adalContext from '../helpers/adalConfig';

@observer
export default class ConnectorOverview extends React.Component {
  private network: any;

  componentDidMount() {
    const company = adalContext.getDomain(adalContext.getUsername()) || 'xAMPLcorp';
    const margin = {
      top:20,
      right:20,
      bottom:20,
      left:20
    }
    const nodes = [
      { id: 1, label: 'Catena-X Core Services\nConnector', x: -300, y: -300, color: '#BAC938', margin: margin},
      { id: 2, label: 'SAP Material Traceability\nConnector', x: 0, y: 300, color: '#1661BE', margin: margin },
      { id: 3, label: 'CO2 Footprint\nConnector', x: -0, y: -0, color: '#969696', margin: margin },
      { id: 5, label: `${company}\nConnector`, x: 300, y: 300, color: '#E20074', margin: margin },
      { id: 6, label: 'ZF\nConnector', x: 450, y: 300, color: 'rgb(17,121,191)', margin: margin },
      { id: 7, label: 'Bosch\nConnector', x: 450, y: 450, color: '#E00420', margin: margin }
    ];

    // create an array with edges
    const edges = [
      { from: 1, to: 5, arrows: "to, from" },
      { from: 2, to: 5, arrows: "to, from" },
      { from: 3, to: 5, arrows: "to, from" },
      { from: 6, to: 5, arrows: "to, from" },
      { from: 7, to: 5, arrows: "to, from" }
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
    this.network.on("doubleClick", function (params) {
      if (params.nodes.length === 1) {
          var node = params.nodes[0];
          if(node==1) {
            window.open(process.env.REACT_APP_CONNECTOR_ENDPOINT, '_blank');
          } else if (node==5) {
            window.open(process.env.REACT_APP_BROKER_ENDPOINT, '_blank');
          }
      }
  });
  }

  public render() {
    return (
      <div className='w100pc pt18 h100pc df fdc'>
        <div id='mynetwork' className='w100pc h600'/>
      </div>
    );
  }
}