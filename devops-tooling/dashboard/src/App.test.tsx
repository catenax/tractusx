import React from 'react';
import { render, screen } from '@testing-library/react';
import App from './App';
import { BrowserRouter } from 'react-router-dom';

const renderWithRouter = (ui, {route = '/'} = {}) => {
  window.history.pushState({}, 'Test page', route)

  return render(ui, {wrapper: BrowserRouter})
}

test('renders Login by default', () => {
  render(<App />);
  const linkElement = screen.getByTestId("login");
  expect(linkElement).toBeInTheDocument();
});
