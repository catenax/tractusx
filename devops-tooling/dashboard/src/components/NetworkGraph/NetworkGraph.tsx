import React, {useEffect, useRef, useState} from 'react';
import Link from '../Link/Link';
import Node from '../Node/Node';
import ForceD3 from './../../d3/Force'


export default function NetworkGraph(props) {
  const ref = useRef(null);
  const [width, setWidth] = useState(640);
  const [height, setHeight] = useState(480);
  const viewBox = `${-width/2} ${-height/2} ${width} ${height}`

  useEffect(initVis, [ props ]);
  useEffect(handleResizeEvent, []);
  
  function handleResizeEvent() {
    let resizeTimer;
    const handleResize = () => {
      clearTimeout(resizeTimer);
      resizeTimer = setTimeout(function() {
        setWidth(window.innerWidth);
        setHeight(window.innerHeight);
      }, 300);
    };
    window.addEventListener('resize', handleResize);

    return () => {
      window.removeEventListener('resize', handleResize);
    };
  }

  function initVis() {
    if(props) {
      const d3Props = {
        nodes: props.nodes,
        links: props.links
      };
      
      new ForceD3(ref.current, d3Props);
    }
  }
  
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
