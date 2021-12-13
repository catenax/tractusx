

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
