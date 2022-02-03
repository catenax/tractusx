import { useEffect } from 'react';
import renderForceGraph from '../../d3/Force';

export default function NetworkGraph(props) {
  useEffect(() => {
    renderForceGraph(props.nodes, props.links, 'network-graph', { width: props.parentSize.width, height: props.parentSize.height, onClick: props.onNodeClick });
  }, [props]);

  return (<div id="network-graph" style={{ backgroundColor: '#777777', height: '100%' }} />);
}
