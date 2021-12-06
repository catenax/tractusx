

import { render, screen } from '@testing-library/react';
import Dashboard from './Dashboard'

jest.mock('../NetworkGraph/NetworkGraph', () => () => (<div>Hello World</div>));

describe('shallow rendering <Dashboard />', () => {
   
  test('svg div', () => {
    render(<Dashboard />);
    // find svg element 
    const dashboardElement = screen.getByTestId('dashboard');
    expect(dashboardElement).toBeInTheDocument();
  });
})

// describe('deep rendering <Dashboard />', () => {
//   test('renders connector 1', () => {
//     render(<Dashboard />);
//     const dashboardElement = screen.getByText("Connector 1");
//     expect(dashboardElement).toBeInTheDocument();
//   });
// })