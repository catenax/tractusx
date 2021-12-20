
import { render,screen } from "@testing-library/react";
import NetworkGraph from "./NetworkGraph";
import data from '../../pages/Dashboard/data.json';

const nodesData = data.nodes.map((d: any) => Object.assign({}, d));
const linksData = data.links;
const parentSize = {width:100,height:100};

describe("NetworkGraph rendering", () => {

  test('it renders connectors', () => {

    render(<NetworkGraph  nodes={nodesData} links={linksData} parentSize={parentSize} />);
    const connectorElement = screen.getByText('Connector 1');
    expect(connectorElement).toBeInTheDocument();
    const connectorElement2 = screen.getByText('Connector 2');
    expect(connectorElement2).toBeInTheDocument();

  });

  test('it matches snapshots',()=> {
    let graphRender =  render(<NetworkGraph  nodes={nodesData} links={linksData} parentSize={parentSize} />);
    expect(graphRender).toMatchSnapshot();
  })

});
