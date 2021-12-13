import { render } from "@testing-library/react";
import Link from "./Link";

describe("Link rendering", () => {

  test('it renders Link with dotted line', () => {
    let line  = render( <svg> <Link  status={"inactive"}  /></svg>);
    expect(line).toMatchSnapshot();
  });

  test('it renders Link with straight line', () => {
    let line  = render( <svg> <Link  status={null}  /></svg>);
    expect(line).toMatchSnapshot();
  });
});
