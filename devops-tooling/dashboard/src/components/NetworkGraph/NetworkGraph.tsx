import * as d3 from 'd3';
import { Simulation, SimulationNodeDatum } from 'd3';
import React, { useState, useEffect, useRef } from 'react'
import Nodes from '../Nodes/Nodes';

export default function NetworkGraph(props) {
  const ref = useRef(null);
  const width = 640;
  const height = 480;
  let simulation: any;
  let svg: any;
  let nodes: any;
  let links: any;
  let labels: any;

  useEffect(() => {
    createForceLayout();
  })

  const createForceLayout = () => {
    svg = d3.select(ref.current).append('svg')
      .attr("class", "graph")
      .attr('viewBox', `${-width/2} ${-height/2} ${width} ${height}`)
      .attr('width', width)
      .attr('height', height);

    simulation = d3.forceSimulation(props.data.nodes)
      .force("link", d3.forceLink(props.data.links))
      .force("charge", d3.forceManyBody().strength(-5000)) // This adds repulsion between nodes. Play with the -400 for the repulsion strength 
      .force("x", d3.forceX())
      .force("y", d3.forceY());
    
    links = svg
      .append("g")
      .attr("stroke", "#999")
      .attr("stroke-opacity", 0.6)
      .selectAll("line")
      .data(props.data.links)
      .join("line")
      .attr("stroke-width", '2');
    
    nodes = new Nodes(svg, props.data.nodes);

    labels = svg.append("g")
      .attr("class", "labels")
      .selectAll("text")
      .data(props.data.nodes)
      .enter()
      .append("text")
      .attr('text-anchor', 'middle')
      .attr('dominant-baseline', 'central')
      .text((d: any) => d.name)
    
    simulation.on("tick", () => {
      positionForceElements();
    });
    nodes.items.call(drag(simulation));
    labels.call(drag(simulation));
  }

  function positionForceElements() {
    //update link positions
    links
      .attr("x1", (d: any) => d.source.x)
      .attr("y1", (d: any) => d.source.y)
      .attr("x2", (d: any) => d.target.x)
      .attr("y2", (d: any) => d.target.y);

  // update node positions
    nodes.items
      .attr("cx", (d: any) => d.x)
      .attr("cy", (d: any) => d.y);

    labels
      .attr("x", (d: any) => d.x)
      .attr("y", (d: any) => d.y);
  }

  function drag(simulation:Simulation<SimulationNodeDatum, undefined>) {    
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
    <div className="graph" ref={ref}>
    </div>
  )
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
