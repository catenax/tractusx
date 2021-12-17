import './Dashboard.scss';
import data from './data.json';
import NetworkGraph from '../../components/NetworkGraph/NetworkGraph';
import Grid from '@mui/material/Grid'
import useAuth from '../../Auth/useAuth';
import Node from '../../Types/Node';
import { useEffect, useRef } from 'react';

export default function Dashboard() {
  let ref = useRef(null);
  const auth = useAuth();
  const nodesData = data.nodes.map((d: any) => Object.assign({}, d));
  let linksData = [] as Node[];

  if (auth.user==="admin"){
    linksData = data.links;
  }

  return (
    <Grid container direction="column" className="dashboard" data-testid="dashboard" ref={ref} sx={{height: 'calc(100% - 64px)'}}>
      <NetworkGraph nodes={nodesData} links={linksData} parentRef={ref}></NetworkGraph>
    </Grid>
  )
}
