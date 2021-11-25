/*
Author: Eli Elad Elrom
Website: https://EliElrom.com
License: MIT License
Component: src/component/Dashboard/Dashboard.tsx

Created with;
$ npx generate-react-cli component Dashboard --type=d3class

*/

import React, { RefObject } from 'react'
import './Dashboard.scss'
import * as d3 from 'd3'
import data from './data.json';
import { Simulation, SimulationNodeDatum } from 'd3';

export default class Dashboard extends React.PureComponent<IDashboardProps, IDashboardState> {
  ref: RefObject<HTMLDivElement>;
  simulation: any;
  svg: any;
  nodes: any;
  links: any;
  labels: any;
  hierarchy: any;

  constructor(props: IDashboardProps) {
    super(props)
    this.state = {
      width: 640,
      height: 480,
      nodes: data.nodes.map((d: any) => Object.assign({}, d)),
      links: data.links
    }
    this.ref = React.createRef()
  }

  componentDidMount() {
    this.createForceLayout();
  }

  createForceLayout(){
    this.svg = d3.select(this.ref.current).append('svg')
      .attr("class", "graph")
      .attr('viewBox', `${-this.state.width/2} ${-this.state.height/2} ${this.state.width} ${this.state.height}`)
      .attr('width', this.state.width)
      .attr('height',  this.state.height);

    this.simulation = d3.forceSimulation(this.state.nodes)
      .force("link", d3.forceLink(this.state.links))
      .force("charge", d3.forceManyBody().strength(-2000))         // This adds repulsion between nodes. Play with the -400 for the repulsion strength 
      .force("x", d3.forceX())
      .force("y", d3.forceY());

    this.nodes = this.svg
      .append("g")
      .attr("stroke", "#fff")
      .attr("stroke-width", 2)
      .selectAll("circle")
      .data(this.state.nodes)
      .join("circle")
      .attr("r", 30)
      .attr("fill", '#69b3a2')

    this.links = this.svg
      .append("g")
      .attr("stroke", "#999")
      .attr("stroke-opacity", 0.6)
      .selectAll("line")
      .data(this.state.links)
      .join("line")
      .attr("stroke-width", '2');

    this.labels = this.svg.append("g")
      .attr("class", "labels")
      .selectAll("text")
      .data(this.state.nodes)
      .enter()
      .append("text")
      .attr('text-anchor', 'middle')
      .attr('dominant-baseline', 'central')
      .attr("class", (d: any) => d.gender)
      .text((d: any) => d.name)


      function drag(simulation:Simulation<SimulationNodeDatum, undefined>) {    
        function dragstarted(event:CustomeEvent) {
          if (!event.active) simulation.alphaTarget(0.3).restart();
          event.subject.fx = event.subject.x;
          event.subject.fy = event.subject.y;
        }
        
        function dragged(event:CustomeEvent) {
          event.subject.fx = event.x;
          event.subject.fy = event.y;
        }
        
        function dragended(event:CustomeEvent) {
          if (!event.active) simulation.alphaTarget(0);
          event.subject.fx = null;
          event.subject.fy = null;
        }
        
        return d3.drag()
          .on("start", dragstarted)
          .on("drag", dragged)
          .on("end", dragended);
      }
      
    this.simulation.on("tick", () => {
      this.positionForceElements();
    });
    this.nodes.call(drag(this.simulation));
    this.labels.call(drag(this.simulation));

  }

  positionForceElements() {
    //update link positions
    this.links
      .attr("x1", (d: any) => d.source.x)
      .attr("y1", (d: any) => d.source.y)
      .attr("x2", (d: any) => d.target.x)
      .attr("y2", (d: any) => d.target.y);

  // update node positions
    this.nodes
      .attr("cx", (d: any) => d.x)
      .attr("cy", (d: any) => d.y);

    this.labels
      .attr("x", (d: any) => d.x)
      .attr("y", (d: any) => d.y);
  }

  render() {
    return (
      <div className="dashboard" ref={this.ref}>
      </div>
    )
  }
}

interface IDashboardProps {
  // TODO

}

interface IDashboardState {
  width: number,
  height: number,
  nodes: any, //needs to be its own type
  links: { source: number, target: number }[]  //needs to be its own type
}

interface CustomeEvent {
  subject: {
    fx: any; 
    fy: any;
    x:any;
    y:any;
  }; 
  x: any;
  y: any; 
  active:any;
}
