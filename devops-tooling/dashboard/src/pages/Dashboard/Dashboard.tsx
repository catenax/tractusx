import './Dashboard.scss';
import data from './data.json';
import NetworkGraph from '../../components/NetworkGraph/NetworkGraph';
import Grid from '@mui/material/Grid'
import useAuth from '../../Auth/useAuth';
import Node from '../../Types/Node';

export default function Dashboard() {
  const auth =useAuth();
  const nodesData = data.nodes.map((d: any) => Object.assign({}, d));
  let linksData = [] as Node[];

  if (auth.user==="admin"){
    linksData = data.links;
  }
  return (
    <Grid container direction="column" className="dashboard" data-testid="dashboard">
      <NetworkGraph nodes={nodesData} links={linksData}></NetworkGraph>
    </Grid>
  )
}
