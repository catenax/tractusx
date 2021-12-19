import * as d3 from 'd3';
import { Simulation, SimulationNodeDatum } from 'd3';

export default class ForceD3 {
  containerEl;
  props;
  width;
  height;
  svg;
  nodes;
  labels;
  links;

  constructor(containerEl, props, width, height){
    this.containerEl = containerEl;
    this.props = props;
    this.width = width;
    this.height = height;
    this.svg = d3.select(containerEl);
    this.nodes = this.svg.selectAll(".node circle").data(props.nodes);
    this.labels = this.svg.selectAll(".node text").data(props.nodes);
    this.links = this.svg.selectAll(".link").data(props.links);

    this.init();
  }

  init(){
    const simulation = d3.forceSimulation(this.props.nodes)
      .force("link", d3.forceLink(this.props.links))
      .force("charge", d3.forceManyBody().strength(-3000)) // This adds repulsion between nodes.
      .force("x", d3.forceX())
      .force("y", d3.forceY());

    simulation.on("tick", () => {
      this.positionForceElements();
    });
    this.nodes.call(this.drag(simulation, this.width, this.height));
    this.labels.call(this.drag(simulation, this.width, this.height));
  }

  positionForceElements() {
    this.nodes
      .attr("cx", (d: any) => d.x)
      .attr("cy", (d: any) => d.y);
    this.labels
      .attr("x", (d: any) => d.x)
      .attr("y", (d: any) => d.y);
    this.links
      .attr("x1", (d: any) => d.source.x)
      .attr("y1", (d: any) => d.source.y)
      .attr("x2", (d: any) => d.target.x)
      .attr("y2", (d: any) => d.target.y);
  }

  drag(simulation: Simulation<SimulationNodeDatum, undefined>, width, height) {
    function dragstarted(event) {
      if (!event.active) simulation.alphaTarget(0.3).restart();
      event.subject.fx = event.subject.x;
      event.subject.fy = event.subject.y;
    }

    function dragged(event) {
      console.log('new');
      const nodeRadius = 30;
      const halfWidth = width/2 - nodeRadius;
      const halfHeight = height/2 - nodeRadius;
      console.log(width);
      console.log(event.x);
      console.log(halfWidth);
      console.log(event.x >= -halfWidth && event.x <= halfWidth);
      if (event.x >= -halfWidth && event.x <= halfWidth) event.subject.fx = event.x;
      if (event.y >= -halfHeight && event.y <= halfHeight) event.subject.fy = event.y;
    }

    function dragended(event) {
      if (!event.active) simulation.alphaTarget(0);
      event.subject.fx = null;
      event.subject.fy = null;
    }

    return d3.drag()
      .on("start", dragstarted)
      .on("drag", dragged)
      .on("end", dragended);
  }
}

/* interface CustomEvent {
  subject: {
    fx: any;
    fy: any;
    x:any;
    y:any;
  };
  x: any;
  y: any;
  active:any;
} */
