import data from './data.json';
import NetworkGraph from '../../components/NetworkGraph/NetworkGraph2';
import Grid from '@mui/material/Grid'
import useAuth from '../../Auth/useAuth';
import Node from '../../Types/Node';
import { useEffect, useRef, useState } from 'react';
import theme from '../../Theme';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button'
import Datepicker from '../../components/Datepicker/Datepicker';
import Link from '../../Types/Link';
import { isAfter, isBefore, isEqual } from 'date-fns';

export default function Dashboard() {
  const auth = useAuth();
  const ref = useRef<HTMLDivElement>(null);
  const [size, setSize] = useState<any>({width: null, height: null});
  const [filterStartDate, setFilterStartDate] = useState(null);
  const [filterEndDate, setFilterEndDate] = useState(null);
  const [minDate, setMinDate] = useState(null);
  const [searchTerm,setSearchTerm] = useState('');
  const [nodesData, setNodesData] = useState<Node[]>(data.nodes as Node[]);
  const [linksData, setLinksData] = useState<Link[]>(auth.user==="admin" ? data.links as Link[] : []);

  const updateDimensions = () => {
    if (ref.current) setSize({
      width: ref.current.offsetWidth,
      height: ref.current.offsetHeight
    });
  };

  const onFilter = () => {
    let filteredNodes = data.nodes.map((d: any) => Object.assign({}, d));
    let filteredLinks = data.links as Link[];

    if (searchTerm){
      filteredNodes = filteredNodes.filter(node => node.name.toLowerCase().includes(searchTerm.toLocaleLowerCase()));
    }
    if (filterStartDate){
      filteredLinks = filteredLinks.filter(link => {
        const issued = Date.parse(link.issued);
        return isAfter(issued, filterStartDate) || isEqual(issued, filterStartDate);
      })
    }
    if (filterEndDate){
      filteredLinks = filteredLinks.filter(link => {
        const issued = Date.parse(link.issued);
        return isEqual(issued, filterEndDate) || isBefore(issued, filterEndDate);
      })
    }

    const nodeIds = new Set(filteredNodes.map(node => node.id));

    filteredLinks = filteredLinks.filter(link => {

      if (nodeIds.has(link.source) && nodeIds.has(link.target)){
        return true;
      }

      return false;

    });

    setLinksData(filteredLinks);
    setNodesData(filteredNodes);
  }

  const onStartDateChange = (value) => {
    setMinDate(value);
    setFilterStartDate(value);
  }

  const handleSearchChange = (e) => {
    setSearchTerm(e.target.value)
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
          <TextField
            label="Search Connector"
            variant="outlined"
            fullWidth
            value={searchTerm}
            onChange={handleSearchChange}  />
        </Grid>
        {auth.user==="admin" &&
          <>
            <Grid item xs={3}>
              <Datepicker title="Start Date" setValue={onStartDateChange} value={filterStartDate}></Datepicker>
            </Grid>
            <Grid item xs={3}>
              <Datepicker title="End Date" minDate={minDate} setValue={setFilterEndDate} value={filterEndDate}></Datepicker>
            </Grid>
          </>
        }
        <Grid item xs={2}>
          <Button variant="contained" color="primary" onClick={onFilter}>Search</Button>
        </Grid>
      </Grid>
      <Grid container direction="column" data-testid="dashboard" ref={ref} sx={{height: `calc(100% - ${theme.spacing(8)})`}}>
         <NetworkGraph nodes={nodesData} links={linksData} parentSize={size}></NetworkGraph>
      </Grid>
    </>
  )
}
