import React from 'react';
import { render, screen } from '@testing-library/react';
import App from './App';

test('renders Login by default', () => {
  render(<App />);
  const linkElement = screen.getByTestId("login");
  expect(linkElement).toBeInTheDocument();
});

test('renders still Login when redirect to dashboard', () => {
  render(<App />);
  window.history.pushState({}, 'Test page', '/dashboard')
  const linkElement = screen.getByTestId("login");
  expect(linkElement).toBeInTheDocument();
});
