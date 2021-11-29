/*
Author: Eli Elad Elrom
Website: https://EliElrom.com
License: MIT License
Component: src/component/Dashboard/Dashboard.tsx

Created with;
$ npx generate-react-cli component Dashboard --type=d3class

*/

import './Dashboard.scss';
import data from './data.json';
import NetworkGraph from '../NetworkGraph/NetworkGraph';

export default function Dashboard() {
  const nodesData = data.nodes.map((d: any) => Object.assign({}, d));
  const linksData = data.links;

  return (
    <div className="dashboard">
      <NetworkGraph nodes={nodesData} links={linksData}></NetworkGraph>
    </div>
  )
}
