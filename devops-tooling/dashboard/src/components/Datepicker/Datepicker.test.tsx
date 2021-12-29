import { render,screen,fireEvent } from "@testing-library/react";
import Datepicker from "./Datepicker";


describe("Datepicker tests",()=>{

  test("datepicker should render properly",()=>{

    jest.useFakeTimers('modern');
    jest.setSystemTime(new Date("2021-01-01"))

    expect(render(<Datepicker />)).toMatchSnapshot();

  })

  test.skip("user can edit datepicker",async ()=> {

    render(<Datepicker title="Some Date" />);
    const element = screen.getByLabelText(/Some Date/i);
    fireEvent.mouseDown(element);
    await fireEvent.change(element, {target: {value: '10122020'}});
    expect(element).toHaveValue('12/12/2020');

  })

})