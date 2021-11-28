/*
Author: Eli Elad Elrom
Website: https://EliElrom.com
License: MIT License
Component: src/component/Node/Node.tsx

Created with;
$ npx generate-react-cli component Node --type=d3

*/

import './Nodes.scss'

export default class Nodes {
  shape = 'circle';
  readonly svg: any;
  readonly items: any;


  constructor(svg: any, nodes: any) {
    this.svg = svg;
    this.items = this.svg.append("g")
      .selectAll(this.shape)
      .data(nodes)
      .join(this.shape)
      .attr("class", "node")
  }
}
