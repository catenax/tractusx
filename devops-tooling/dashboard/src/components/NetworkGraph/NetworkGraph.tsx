import React, {useEffect, useRef, useState} from 'react';
import Link from '../Link/Link';
import Node from '../Node/Node';
import ForceD3 from './../../d3/Force'


export default function NetworkGraph(props) {
  const ref = useRef(null);
  const [width, setWidth] = useState(0);
  const [height, setHeight] = useState(0);
  const viewBox = `${-width/2} ${-height/2} ${width} ${height}`

  const initVis = () => {
    if (props) {
      const d3Props = {
        nodes: props.nodes,
        links: props.links
      };
      new ForceD3(ref.current, d3Props, width, height);
    }
  }

  useEffect(() => {
    setWidth(props.parentSize.width);
    setHeight(props.parentSize.height);
  }, [props.parentSize])
  useEffect(initVis, [width, height, props]);

  return (
    <svg width={width} height={height} ref={ref} viewBox={viewBox}>
      <g>
        {props.links.map(link =>
          <Link key={link.id} data={link} status={link.status || null}></Link>
        )}
      </g>
      <g>
        {props.nodes.map(node =>
          <Node key={node.id} data={node}></Node>
        )}
      </g>
    </svg>
  );
}
