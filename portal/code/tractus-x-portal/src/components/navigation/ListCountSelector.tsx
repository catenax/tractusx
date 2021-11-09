import { DefaultButton } from "@fluentui/react"

export default function ListCountSelector(props){
  const items = [10, 50, 100]
  function isCurrentCount(count){
    return count === props.activeCount;
  }

  return(
    <div className="df jcfe">
      <p>Number of visible items:</p>
      {items.map(count => 
        <DefaultButton key={`numItem${count}`} text={`${count}`} className="ml20" disabled={isCurrentCount(count)} onClick={() => props.onCountClick(count)}/>
      )}
    </div>
  )
}
