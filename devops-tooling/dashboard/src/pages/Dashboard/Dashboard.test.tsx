import { render, screen } from '@testing-library/react';
import Dashboard from './Dashboard'
import useAuth from '../../Auth/useAuth';

jest.mock('../../components/NetworkGraph/NetworkGraph', () => () => (<div>Hello World</div>));
jest.mock("../../Auth/useAuth");

describe('shallow rendering <Dashboard />', () => {

  test('svg div', () => {
    useAuth.mockReturnValue({user:"user"})
    render(<Dashboard />);
    // find svg element
    const dashboardElement = screen.getByTestId('dashboard');
    expect(dashboardElement).toBeInTheDocument();
  });

  test("snapshot for user",()=>{
    useAuth.mockReturnValue({user:"user"})
    const dashBoard = render(<Dashboard />);
    expect(dashBoard).toMatchSnapshot()
  })

  test("snapshot for admin",()=>{
    useAuth.mockReturnValue({user:"admin"})
    const dashBoard = render(<Dashboard />);
    expect(dashBoard).toMatchSnapshot()
  })

})
