import { render,screen } from "@testing-library/react";
import Node from "./Node";


describe("Node rendering", () => {

  test("renders node properly", () => {
    let data  = {name:"connector 1"}
    render( <svg> <Node  data={data} /></svg>);
    const circleText = screen.getByText('connector 1');
    expect(circleText).toBeInTheDocument();
  })

  test("Node matches snapshot", () => {
    let data  = {name:"connector 1"}
    let nodeRender = render( <svg> <Node  data={data} /></svg>);
    expect(nodeRender).toMatchSnapshot();
  })


});
