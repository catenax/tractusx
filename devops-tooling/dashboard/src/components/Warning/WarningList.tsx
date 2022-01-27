import WarningListItem from "./WarningListItem";

export default function WarningList(){

  return (
    <>
      {Array(4).fill(0).map((item,index)=> <WarningListItem key={index} /> )}
    </>
  )

}