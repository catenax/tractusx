/*
Author: Eli Elad Elrom
Website: https://EliElrom.com
License: MIT License
Component: src/component/Dashboard/Dashboard.tsx

Created with;
$ npx generate-react-cli component Dashboard --type=d3class

*/

import './Dashboard.scss';
import data from './data.json';
import NetworkGraph from '../../NetworkGraph/NetworkGraph';
import Grid from '@mui/material/Grid'
import Button from '@mui/material/Button'
import useAuth from '../../../Auth/useAuth';

export default function Dashboard() {
  const nodesData = data.nodes.map((d: any) => Object.assign({}, d));
  const linksData = data.links;
  const auth = useAuth();

  function handleClick () {
    auth.signOut(()=>{});
  }

  return (
    <Grid container direction="column" className="dashboard" data-testid="dashboard">
      <Grid container justifyContent="center" sx={{p: 2}}>
        <span>DevOps Tooling</span>
        <Button variant="contained" color="primary" onClick={handleClick}>Logout</Button>
      </Grid>
      <NetworkGraph nodes={nodesData} links={linksData}></NetworkGraph>
    </Grid>
  )
}
