import renderForceGraph from '../../d3/Force2';
import { useEffect } from 'react';


export default function NetworkGraph(props) {


  useEffect(() => {
    renderForceGraph(props.nodes, props.links,'network-graph', {width: props.parentSize.width, height: props.parentSize.height});
  }, [props])

  return ( <div id="network-graph"> </div> );
}
