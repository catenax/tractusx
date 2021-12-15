import './Dashboard.scss';
import data from './data.json';
import NetworkGraph from '../../components/NetworkGraph/NetworkGraph';
import Grid from '@mui/material/Grid'
import Button from '@mui/material/Button'
import useAuth from '../../Auth/useAuth';

export default function Dashboard() {
  const nodesData = data.nodes.map((d: any) => Object.assign({}, d));
  const linksData = data.links;
  const auth = useAuth();

  function handleClick () {
    auth.signOut(()=>{});
  }

  return (
    <Grid container direction="column" className="dashboard" data-testid="dashboard">
      <Grid container justifyContent="center" sx={{p: 2, bgcolor: 'white' }}>
        <span>DevOps Tooling</span>
        <Button variant="contained" color="primary" onClick={handleClick}>Logout</Button>
      </Grid>
      <NetworkGraph nodes={nodesData} links={linksData}></NetworkGraph>
    </Grid>
  )
}
