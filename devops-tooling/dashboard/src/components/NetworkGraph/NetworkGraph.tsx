import { useEffect } from 'react';
import renderForceGraph from '../../d3/Force';

export default function NetworkGraph({
  nodes, links, parentSize, onNodeClick,
}) {
  useEffect(() => {
    renderForceGraph(nodes, links, 'network-graph', { width: parentSize.width, height: parentSize.height, onClick: onNodeClick });
  }, [nodes, links, parentSize, onNodeClick]);

  return (<div id="network-graph" style={{ backgroundColor: '#777777', height: '100%' }} />);
}
