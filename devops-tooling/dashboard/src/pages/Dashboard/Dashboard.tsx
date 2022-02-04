import Grid from '@mui/material/Grid';
import { useEffect, useRef, useState } from 'react';
import { Button, Typography } from '@mui/material';
import {
  isAfter, isBefore, isEqual, startOfDay, endOfDay, parseISO,
} from 'date-fns';
import data from './data.json';
import sdData from './sd-data.json';
import NetworkGraph from '../../components/NetworkGraph/NetworkGraph';
import useAuth from '../../Auth/useAuth';
import INode from '../../Types/Node';
import theme from '../../Theme';
import DashboardFilter from '../../components/Filter/DashboardFilter';
import ILink from '../../Types/Link';
import NodeSelfDescription from '../../components/NodeSelfDescription/NodeSelfDescription';

const idURL = 'https://w3id.org/idsa/autogen/baseConnector/';

export default function Dashboard() {
  const cloneData = JSON.parse(JSON.stringify(data));
  const auth = useAuth();
  const ref = useRef<HTMLDivElement>(null);
  const [size, setSize] = useState<any>({ width: null, height: null });
  const [nodesData, setNodesData] = useState<INode[]>(cloneData.nodes as INode[]);
  // eslint-disable-next-line max-len
  const [linksData, setLinksData] = useState<ILink[]>(auth.isAdmin() ? cloneData.links as ILink[] : []);
  const [showSelfDescription, setShowSelfDescription] = useState<any>(null);

  const updateDimensions = () => {
    if (ref.current) {
      setSize({
        width: ref.current.offsetWidth,
        height: ref.current.offsetHeight,
      });
    }
  };

  const onFilter = (filterStartDate, filterEndDate, searchTerm) => {
    let filteredNodes = cloneData.nodes as INode[];
    let filteredLinks = cloneData.links as ILink[];

    if (searchTerm) {
      filteredNodes = filteredNodes
        .filter(
          (node) => node.name.toLowerCase().includes(searchTerm.toLocaleLowerCase()),
        );
    }

    if (showSelfDescription) {
      const idUrl = showSelfDescription['@id'];
      const id = idUrl.slice(idUrl.lastIndexOf('/') + 1);
      const activeItem = filteredNodes.filter((item) => item.id === Number(id));
      if (!activeItem.length) setShowSelfDescription(null);
    }
    setNodesData(filteredNodes);

    // if user is not admin the we dont need to filter links
    if (!auth.isAdmin()) {
      return;
    }

    if (filterStartDate) {
      const startDate = startOfDay(filterStartDate);
      filteredLinks = filteredLinks.filter((link) => {
        const issued = parseISO(link.issued);
        return isAfter(issued, startDate) || isEqual(issued, startDate);
      });
    }
    if (filterEndDate) {
      const endDate = endOfDay(filterEndDate);
      filteredLinks = filteredLinks.filter((link) => {
        const issued = parseISO(link.issued);
        return isEqual(issued, endDate) || isBefore(issued, endDate);
      });
    }

    const nodeIds = new Set(filteredNodes.map((node) => node.id));

    filteredLinks = filteredLinks.filter((link) => {
      if (nodeIds.has(link.source) && nodeIds.has(link.target)) {
        return true;
      }

      return false;
    });

    setLinksData(filteredLinks);
  };

  const addWarningToNode = () => {
    if (nodesData.length > 0) {
      const n = cloneData.nodes as INode[];
      const randomIndex = Math.floor(Math.random() * n.length);
      n[randomIndex].status = { type: 'warning', text: 'The connection has been interrupted.' };
      setNodesData(n);
    }
  };
  const clickOnNode = (id) => {
    const selectedItem = sdData.filter((item) => item['@id'] === `${idURL}${id}`);
    setShowSelfDescription(selectedItem[0]);
  };

  useEffect(() => {
    window.addEventListener('resize', updateDimensions);
    updateDimensions();
    return () => {
      window.removeEventListener('resize', updateDimensions);
    };
  }, []);

  return (
    <>
      <DashboardFilter onFilter={onFilter} />
      <Grid container direction="column" alignItems="center" data-testid="dashboard" ref={ref} sx={{ height: `calc(100vh - ${theme.spacing(25)})` }}>
        {nodesData.length > 0 && size.height ? (
          <Grid item container>
            <Grid item xs={2}>
              <Typography sx={{ mb: theme.spacing(1) }} variant="subtitle1" component="h3">List of active connectors</Typography>
              {nodesData.map((item) => <Typography key={item.id}>{item.name}</Typography>)}
            </Grid>
            <Grid item xs={showSelfDescription != null ? 7 : 10}>
              <NetworkGraph
                nodes={nodesData}
                links={linksData}
                parentSize={size}
                onNodeClick={clickOnNode}
              />
            </Grid>
            {showSelfDescription != null
              && (
              <NodeSelfDescription
                item={showSelfDescription}
                onClose={setShowSelfDescription}
              />
              )}
            {auth.isAdmin() && (
              <Button variant="contained" color="primary" onClick={addWarningToNode} sx={{ alignSelf: 'start' }}>
                Add Warning
              </Button>
            )}
          </Grid>
        ) : (
          <Grid item xs={12}>
            <Typography variant="h3">No results!</Typography>
            <Typography variant="body1">Please change your filter settings.</Typography>
          </Grid>
        )}
      </Grid>
    </>
  );
}
