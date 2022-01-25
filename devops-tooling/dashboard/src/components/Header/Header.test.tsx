import { render, screen } from "@testing-library/react";
import Header from "./Header";

describe("Header rendering", () => {
  test('it renders Catena-X and Connector Dashboard within the Header', () => {
    render(<Header />);
    const header = screen.getByTestId('header');
    expect(header).toHaveTextContent('Catena-X');
    expect(header).toHaveTextContent('Connector Dashboard');
  });
});
