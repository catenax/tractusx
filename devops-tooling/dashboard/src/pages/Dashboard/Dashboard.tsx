import data from './data.json';
import NetworkGraph from '../../components/NetworkGraph/NetworkGraph';
import Grid from '@mui/material/Grid'
import useAuth from '../../Auth/useAuth';
import Node from '../../Types/Node';
import { useEffect, useRef, useState } from 'react';
import theme from '../../Theme';
import { Button, Typography } from '@mui/material';
import DashboardFilter from '../../components/Filter/DashboardFilter';
import Link from '../../Types/Link';
import { isAfter, isBefore, isEqual, startOfDay,endOfDay,parseISO  } from 'date-fns';

export default function Dashboard() {
  const cloneData  = JSON.parse(JSON.stringify(data))
  const auth = useAuth();
  const ref = useRef<HTMLDivElement>(null);
  const [size, setSize] = useState<any>({width: null, height: null});
  const [nodesData, setNodesData] = useState<Node[]>(cloneData.nodes as Node[]);
  const [linksData, setLinksData] = useState<Link[]>(auth.user==="admin" ? cloneData.links as Link[] : []);

  const updateDimensions = () => {
    if (ref.current) setSize({
      width: ref.current.offsetWidth,
      height: ref.current.offsetHeight
    });
  };

  const onFilter = (filterStartDate, filterEndDate, searchTerm) => {
    let filteredNodes = cloneData.nodes as Node[];
    let filteredLinks = cloneData.links as Link[];

    if (searchTerm){
      filteredNodes = filteredNodes.filter(node => node.name.toLowerCase().includes(searchTerm.toLocaleLowerCase()));
    }

    setNodesData(filteredNodes);

    //if user is not admin the we dont need to filter links
    if (auth.user!=="admin") {
      return;
    }

    if (filterStartDate){
      const startDate = startOfDay(filterStartDate);
      filteredLinks = filteredLinks.filter(link => {
        const issued = parseISO(link.issued);
        return isAfter(issued, startDate) || isEqual(issued, startDate);
      })
    }
    if (filterEndDate){
      const endDate = endOfDay(filterEndDate)
      filteredLinks = filteredLinks.filter(link => {
        const issued = parseISO(link.issued);
        return isEqual(issued, endDate) || isBefore(issued, endDate);
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
  }

  const addWarningToNode = () => {
    if (nodesData.length > 0){
      let n = cloneData.nodes as Node[];
      const randomIndex = Math.floor(Math.random()*n.length);
      n[randomIndex]['status'] = {type: 'warning', text: 'The connection has been interrupted.'};
      setNodesData(n);
    }
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
      <DashboardFilter onFilter={onFilter}></DashboardFilter>
      <Grid container direction="column" alignItems="center" data-testid="dashboard" ref={ref} sx={{height: `calc(100% - ${theme.spacing(8)})`}}>
        {nodesData.length > 0 ?
          <>
            {size.height && <NetworkGraph nodes={nodesData} links={linksData} parentSize={size}></NetworkGraph>}
          </> :
          <Grid item xs={12} sx={{mt: 8}}>
            <Typography variant="h3">No results!</Typography>
            <Typography variant="body1">Please change your filter settings.</Typography>
          </Grid>
        }
      </Grid>
      {auth.user==="admin" &&
        <Button variant="contained" color="primary" onClick={addWarningToNode}>
          Add Warning
        </Button>
      }
    </>
  )
}
