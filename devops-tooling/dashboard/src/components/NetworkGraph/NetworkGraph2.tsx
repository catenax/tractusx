import renderForceGraph from '../../d3/Force2';
import { useEffect } from 'react';


export default function NetworkGraph(props) {


  useEffect(() => {
    renderForceGraph(props.nodes, props.links,'network-graph');
  },[props.nodes,props.links])

  return ( <div id="network-graph" style={{height:'100vh',width:'100%',backgroundColor:'grey'}}> </div> );
}