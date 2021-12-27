import { fireEvent, render,screen } from "@testing-library/react";
import DashboardFilter from "./DashboardFilter";
import useAuth from '../../Auth/useAuth';
import userEvent from "@testing-library/user-event";

jest.mock("../../Auth/useAuth");

describe("DashboardFilter tests", () => {

  test("DashboardFilter should render for normal user", () => {

    useAuth.mockReturnValue({user:"user"})
    const wrapper = render(<DashboardFilter />);
    expect(wrapper).toMatchSnapshot();
  });

  test("DashboardFilter should render for admin user", () => {

    useAuth.mockReturnValue({user:"admin"})
    const wrapper = render(<DashboardFilter />);
    expect(wrapper).toMatchSnapshot();
  });

  test("Dashboard filter should call filter function properly for connector names", () => {

    useAuth.mockReturnValue({user:"admin"})
    const filterFn = jest.fn();
    render(<DashboardFilter onFilter={filterFn} />);
    fireEvent.change( screen.getByTestId("searchText"), {target: {value: 'test'}})
    const leftClick = {button: 0}
    const element = screen.getByRole('button', { name: /Search/i  })
    userEvent.click(element, leftClick);

    expect(filterFn).toHaveBeenCalledWith(null, null, "test");

  })


})