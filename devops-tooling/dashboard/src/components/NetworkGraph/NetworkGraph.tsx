import * as d3 from 'd3';
import { Simulation, SimulationNodeDatum } from 'd3';
import React, {useEffect, useRef, useState} from 'react';
import Link from '../Link/Link';
import Node from '../Node/Node';

export default function NetworkGraph2(props) {
  const ref = useRef(null);
  const width = 640;
  const height = 480;
  const viewBox = `${-width/2} ${-height/2} ${width} ${height}`
  let nodes: any;
  let links: any;
  let labels: any;
  
  useEffect(() => {
    createForceLayout();
  })

  const createForceLayout = () => {
    const svg = d3.select(ref.current)
    nodes = svg.selectAll(".node circle").data(props.nodes)
    labels = svg.selectAll(".node text").data(props.nodes)
    links = svg.selectAll(".link").data(props.links)

    const simulation = d3.forceSimulation(props.nodes)
      .force("link", d3.forceLink(props.links))
      .force("charge", d3.forceManyBody().strength(-5000)) // This adds repulsion between nodes. Play with the -400 for the repulsion strength 
      .force("x", d3.forceX())
      .force("y", d3.forceY());
    
    simulation.on("tick", () => {
      positionForceElements();
    });
    nodes.call(drag(simulation));
    labels.call(drag(simulation));
  }

  function positionForceElements() {
    nodes
      .attr("cx", (d: any) => d.x)
      .attr("cy", (d: any) => d.y);
    labels
      .attr("x", (d: any) => d.x)
      .attr("y", (d: any) => d.y);
    links
      .attr("x1", (d: any) => d.source.x)
      .attr("y1", (d: any) => d.source.y)
      .attr("x2", (d: any) => d.target.x)
      .attr("y2", (d: any) => d.target.y);
  }

  function drag(simulation: Simulation<SimulationNodeDatum, undefined>) {    
    function dragstarted(event: CustomEvent) {
      if (!event.active) simulation.alphaTarget(0.3).restart();
      
      event.subject.fx = event.subject.x;
      event.subject.fy = event.subject.y;
    }
    
    function dragged(event: CustomEvent) {
      event.subject.fx = event.x;
      event.subject.fy = event.y;
    }
    
    function dragended(event: CustomEvent) {
      if (!event.active) simulation.alphaTarget(0);
      event.subject.fx = null;
      event.subject.fy = null;
    }
    
    return d3.drag()
      .on("start", dragstarted)
      .on("drag", dragged)
      .on("end", dragended);
  }
  
  return (
    <svg width={width} height={height} ref={ref} viewBox={viewBox}>
      <g>
        {props.links.map(link =>
          <Link key={link.id} data={link} status={link.status || null}></Link>
        )}
      </g>
      <g>
        {props.nodes.map(node => 
          <Node key={node.id} data={node}></Node>
        )}
      </g>
    </svg>
  );
}

interface CustomEvent {
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
