import './Dashboard.scss';
import data from './data.json';
import NetworkGraph from '../../components/NetworkGraph/NetworkGraph';
import Grid from '@mui/material/Grid'

export default function Dashboard() {
  const nodesData = data.nodes.map((d: any) => Object.assign({}, d));
  const linksData = data.links;


  return (
    <Grid container direction="column" className="dashboard" data-testid="dashboard">
      <NetworkGraph nodes={nodesData} links={linksData}></NetworkGraph>
    </Grid>
  )
}
