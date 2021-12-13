import './Link.scss'

export default function Link(props){

  return (
    <line className={`link ${props.status ? `link--${props.status}` : ""}`}></line>
  )
}
