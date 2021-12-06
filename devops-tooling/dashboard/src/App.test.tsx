import React from 'react';
import { render, screen } from '@testing-library/react';
import App from './App';

test('renders Devops dashboard', () => {
  render(<App />);
  const linkElement = screen.getByText(/DevOps Tooling/i);
  expect(linkElement).toBeInTheDocument();
});