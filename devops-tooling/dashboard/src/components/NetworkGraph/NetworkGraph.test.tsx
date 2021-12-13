
import { render,screen } from "@testing-library/react";
import NetworkGraph from "./NetworkGraph";
import data from '../Dashboard/data.json';

const nodesData = data.nodes.map((d: any) => Object.assign({}, d));
const linksData = data.links;

describe("NetworkGraph rendering", () => {

  test('it renders connectors', () => {

    render(<NetworkGraph  nodes={nodesData} links={linksData}/>);
    const connectorElement = screen.getByText('Connector 1');
    expect(connectorElement).toBeInTheDocument();
    const connectorElement2 = screen.getByText('Connector 2');
    expect(connectorElement2).toBeInTheDocument();

  });

  test('it matches snapshots',()=> {
    let graphRender =  render(<NetworkGraph  nodes={nodesData} links={linksData}/>);
    expect(graphRender).toMatchSnapshot();
  })

});