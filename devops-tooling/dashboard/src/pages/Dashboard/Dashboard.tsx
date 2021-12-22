import data from './data.json';
import NetworkGraph from '../../components/NetworkGraph/NetworkGraph';
import Grid from '@mui/material/Grid'
import useAuth from '../../Auth/useAuth';
import Node from '../../Types/Node';
import { useEffect, useRef, useState } from 'react';
import theme from '../../Theme';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button'
import Datepicker from '../../components/Datepicker/Datepicker';

export default function Dashboard() {
  const [size, setSize] = useState<any>({width: null, height: null});
  const [filterStartDate, setFilterStartDate] = useState(null);
  const [filterEndDate, setFilterEndDate] = useState(null);
  const [minDate, setMinDate] = useState(null);
  const [nodesData, setNodesData] = useState(data.nodes.map((d: any) => Object.assign({}, d)));
  const ref = useRef<HTMLDivElement>(null);
  const auth = useAuth();
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

  const onFilter = () => {
    const filteredNodes = nodesData.filter((elem, index) => index === 0)
    setNodesData(filteredNodes);
  }

  const onStartDateChange = (value) => {
    setMinDate(value);
    setFilterStartDate(value);
  }

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
          <TextField  label="Search Connector" variant="outlined" fullWidth  />
        </Grid>
        <Grid item xs={3}>
          <Datepicker title="Start Date" setValue={onStartDateChange} value={filterStartDate}></Datepicker>
        </Grid>
        <Grid item xs={3}>
          <Datepicker title="End Date" minDate={minDate} setValue={setFilterEndDate} value={filterEndDate}></Datepicker>
        </Grid>
        <Grid item xs={2}>
          <Button variant="contained" color="primary" onClick={onFilter}>Search</Button>
        </Grid>
      </Grid>
      <Grid container direction="column" data-testid="dashboard" ref={ref} sx={{height: `calc(100% - ${theme.spacing(8)})`}}>
        {size.height && <NetworkGraph nodes={nodesData} links={linksData} parentSize={size}></NetworkGraph>}
      </Grid>
    </>
  )
}
