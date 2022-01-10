import data from './data.json';
import sdData from './sd-data.json';
import NetworkGraph from '../../components/NetworkGraph/NetworkGraph';
import Grid from '@mui/material/Grid'
import useAuth from '../../Auth/useAuth';
import INode from '../../Types/Node';
import { useEffect, useRef, useState } from 'react';
import theme from '../../Theme';
import { Button, Link, Typography } from '@mui/material';
import DashboardFilter from '../../components/Filter/DashboardFilter';
import DescriptionList from '../../components/DescriptionList/DescriptionList';
import ILink from '../../Types/Link';
import Close from '@mui/icons-material/Close';
import { isAfter, isBefore, isEqual, startOfDay, endOfDay, parseISO } from 'date-fns';

export default function Dashboard() {
  const cloneData  = JSON.parse(JSON.stringify(data))
  const auth = useAuth();
  const ref = useRef<HTMLDivElement>(null);
  const [size, setSize] = useState<any>({width: null, height: null});
  const [nodesData, setNodesData] = useState<INode[]>(cloneData.nodes as INode[]);
  const [linksData, setLinksData] = useState<ILink[]>(auth.user==="admin" ? cloneData.links as ILink[] : []);
  const [showSelfDescription, setShowSelfDescription] = useState<any>(null);

  const updateDimensions = () => {
    if (ref.current) setSize({
      width: ref.current.offsetWidth,
      height: ref.current.offsetHeight
    });
  };

  const onFilter = (filterStartDate, filterEndDate, searchTerm) => {
    let filteredNodes = cloneData.nodes as INode[];
    let filteredLinks = cloneData.links as ILink[];

    if (searchTerm){
      filteredNodes = filteredNodes.filter(node => node.name.toLowerCase().includes(searchTerm.toLocaleLowerCase()));
    }

    if (showSelfDescription){
      const idUrl = showSelfDescription['@id'];
      const id = idUrl.slice(idUrl.lastIndexOf('/') + 1);
      const activeItem = filteredNodes.filter(item => {
        return item.id === Number(id);
      })
      if (activeItem.length === 0) setShowSelfDescription(null);
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
      let n = cloneData.nodes as INode[];
      const randomIndex = Math.floor(Math.random()*n.length);
      n[randomIndex]['status'] = {type: 'warning', text: 'The connection has been interrupted.'};
      setNodesData(n);
    }
  }
  const clickOnNode = (id) => {
    const item = sdData.filter(item => item['@id'] === `https://w3id.org/idsa/autogen/baseConnector/${id}`);
    setShowSelfDescription(item[0])
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
      <Grid container direction="column" alignItems="center" data-testid="dashboard" ref={ref} sx={{height: `calc(100vh - ${theme.spacing(25)})`}}>
        {nodesData.length > 0 && size.height ?
          <Grid item container>
            <Grid item xs={showSelfDescription != null ? 9 : 12}>
              <NetworkGraph nodes={nodesData} links={linksData} parentSize={size} onNodeClick={clickOnNode}></NetworkGraph>
            </Grid>
            {showSelfDescription != null &&
              <Grid item container direction="column" xs={3} sx={{p: theme.spacing(2), border: '1px solid #000'}}>
                <Link
                  color="secondary"
                  onClick={() => setShowSelfDescription(null)}
                  sx={{alignSelf: 'end', cursor: 'pointer'}}
                >
                  <Close />
                </Link>
                <Typography variant="h5" sx={{mb: theme.spacing(2)}}>
                  <Link href={showSelfDescription['@id']} target="_blank">{showSelfDescription['ids:title'][0]['@value']}</Link>
                </Typography>
                <DescriptionList topic={'Format'} link={showSelfDescription['@context'].ids}></DescriptionList>
                <DescriptionList topic={'Type'} description={showSelfDescription['@type']}></DescriptionList>
                <DescriptionList topic={'Maintainer'} link={showSelfDescription['ids:maintainer']["@id"]}></DescriptionList>
                <DescriptionList topic={'Curator'} link={showSelfDescription['ids:curator']['@id']}></DescriptionList>
                <DescriptionList topic={'Version'} description={showSelfDescription['ids:outboundModelVersion']}></DescriptionList>
              </Grid>
            }
            {auth.user==="admin" &&
              <Button variant="contained" color="primary" onClick={addWarningToNode} sx={{alignSelf: 'start'}}>
                Add Warning
              </Button>
            }
          </Grid> :
          <Grid item xs={12}>
            <Typography variant="h3">No results!</Typography>
            <Typography variant="body1">Please change your filter settings.</Typography>
          </Grid>
        }
      </Grid>
    </>
  )
}
