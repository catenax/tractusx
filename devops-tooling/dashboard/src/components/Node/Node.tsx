import './Node.scss'

export default function Node(props){

  return(
    <g className="node">
      <circle className="node__shape"></circle>
      <text textAnchor='middle' dominantBaseline='central' className="node__text">{props.data.name}</text>
    </g>
  )

}
