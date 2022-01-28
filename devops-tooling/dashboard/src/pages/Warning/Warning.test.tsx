import { render,screen } from "@testing-library/react";
import Warning from "./Warning";


describe('warning page tests',()=>{

  test('warning page renders without crashing',()=>{
    render(<Warning />);
    const text = screen.getByText('From');
    expect(text).toBeInTheDocument();
  })

  test('warning page snapshot',()=>{
    const warningPage = render(<Warning />);
    expect(warningPage).toMatchSnapshot();
  })

})
