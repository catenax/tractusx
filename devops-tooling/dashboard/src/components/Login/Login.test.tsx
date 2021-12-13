import { fireEvent, render,screen } from "@testing-library/react";
import Login from "./Login";
import {MemoryRouter} from 'react-router-dom'
import userEvent from "@testing-library/user-event";
import useAuth from "../../Auth/useAuth";

jest.mock("../../Auth/useAuth");

describe("Login rendering", () => {

  test("renders without crashing", () => {

    render(
      <Login /> ,{wrapper: MemoryRouter}
    );
    const linkElement = screen.getAllByText(/Sign In/i);
    expect(linkElement.length).toBe(2);
  })

  test("login test with admin user", () => {
    const mockSignIn = jest.fn();
    useAuth.mockReturnValue({signIn: mockSignIn})
    render(
      <Login  />,{wrapper: MemoryRouter}
    );

    const leftClick = {button: 0}
    const element = screen.getByText('Sign In');

    fireEvent.change( screen.getByTestId("username"), {target: {value: 'admin'}})
    fireEvent.change( screen.getByTestId("password"), {target: {value: 'admin'}})
    userEvent.click(element, leftClick);

    expect(mockSignIn).toHaveBeenCalledTimes(1);

  })

  test("login test with restricted user", () => {
    const mockSignIn = jest.fn();
    useAuth.mockReturnValue({signIn: mockSignIn})
    render(
      <Login  />,{wrapper: MemoryRouter}
    );

    const leftClick = {button: 0}
    const element = screen.getByText('Sign In');

    fireEvent.change( screen.getByTestId("username"), {target: {value: 'user'}})
    fireEvent.change( screen.getByTestId("password"), {target: {value: 'user'}})
    userEvent.click(element, leftClick);

    expect(mockSignIn).toHaveBeenCalledTimes(1);

  })

  test("login test with invalid user", () => {
    const mockSignIn = jest.fn();
    useAuth.mockReturnValue({signIn: mockSignIn})
    render(
      <Login  />,{wrapper: MemoryRouter}
    );

    const leftClick = {button: 0}
    const element = screen.getByText('Sign In');

    fireEvent.change( screen.getByTestId("username"), {target: {value: 'test'}})
    fireEvent.change( screen.getByTestId("password"), {target: {value: 'test'}})
    userEvent.click(element, leftClick);

    expect(mockSignIn).toHaveBeenCalledTimes(0);
    expect(screen.getAllByText('Authentication failed. Please try again!').length).toBe(1);
  })

  test("login test for errors", () => {
    render(
      <Login  />,{wrapper: MemoryRouter}
    );

    const leftClick = {button: 0}
    const element = screen.getByText('Sign In');

    fireEvent.change( screen.getByTestId("username"), {target: {value: ''}})
    fireEvent.change( screen.getByTestId("password"), {target: {value: ''}})
    userEvent.click(element, leftClick);

    expect(screen.getAllByText('This field is required.').length).toBe(2);
  })
});
