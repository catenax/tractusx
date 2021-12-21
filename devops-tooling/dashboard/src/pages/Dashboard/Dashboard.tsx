import './Dashboard.scss';
import data from './data.json';
import NetworkGraph from '../../components/NetworkGraph/NetworkGraph';
import Grid from '@mui/material/Grid'
import useAuth from '../../Auth/useAuth';
import Node from '../../Types/Node';
import { useEffect, useRef, useState } from 'react';
import theme from '../../Theme';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button'

export default function Dashboard() {
  const [size, setSize] = useState<any>({width: null, height: null});
  const ref = useRef<HTMLDivElement>(null);
  const auth = useAuth();
  const nodesData = data.nodes.map((d: any) => Object.assign({}, d));
  let linksData = [] as Node[];

  if (auth.user==="admin"){
    linksData = data.links;
  }

  const updateDimensions = () => {
    if (ref.current) setSize({
      width: ref.current.offsetWidth,
      height: ref.current.offsetHeight
    });
  };

  useEffect(() => {
    window.addEventListener("resize", updateDimensions);
    updateDimensions();
    return () => {
      window.removeEventListener("resize", updateDimensions);
    };
  }, []);

  return (
    <>
      <Grid container spacing={1}>
        <Grid item xs={4}>
          <TextField  label="Search Connector " variant="outlined" fullWidth  />
        </Grid>

        <Grid item xs={4}>
          <TextField  label="Start Date" variant="outlined" fullWidth />
        </Grid>
        <Grid item xs={4}>
          <TextField  label="End Date" variant="outlined" fullWidth />
        </Grid>
        <Grid item xs={3}>
          <Button variant="contained" color="primary" > Search </Button>
        </Grid>
      </Grid>
      <Grid container direction="column" className="dashboard" data-testid="dashboard" ref={ref} sx={{height: `calc(100% - ${theme.spacing(8)})`}}>
        {size.height && <NetworkGraph nodes={nodesData} links={linksData} parentSize={size}></NetworkGraph>}
      </Grid>
    </>
  )
}
