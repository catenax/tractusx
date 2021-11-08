import { DefaultButton, IIconProps } from "@fluentui/react";
import { useEffect, useState } from "react";

export default function Pagination(props){
  const ChevronLeft: IIconProps = { iconName: 'ChevronLeft' };
  const ChevronRight: IIconProps = { iconName: 'ChevronRight' };
  const [isDisabledBefore, setIsDisabledBefore] = useState<boolean | any>(false);

  useEffect(() => {
    setIsDisabledBefore(props.pageNumber === 0);
  });

  return(
    <div className="df jcc w100pc mt20">
      <DefaultButton iconProps={ChevronLeft} onClick={props.onPageBefore} disabled={isDisabledBefore}/>
      <span className="fs20 mr20 ml20 fg5a">page {props.pageNumber + 1}</span>
      <DefaultButton iconProps={ChevronRight} onClick={props.onPageNext} disabled={props.isDisabledNext}/>
    </div>
  )
}
