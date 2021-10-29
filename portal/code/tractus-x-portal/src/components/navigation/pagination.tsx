import { DefaultButton, IIconProps } from "@fluentui/react";
import { useEffect, useState } from "react";

export default function Pagination(props){
  const ChevronLeft: IIconProps = { iconName: 'ChevronLeft' };
  const ChevronRight: IIconProps = { iconName: 'ChevronRight' };
  const [isDisabledLeft, setIsDisabledLeft] = useState<boolean | any>(false);

  useEffect(() => {
    setIsDisabledLeft(props.pageNumber === 1);
  });

  return(
    <div className="df jcc mt20">
      <DefaultButton iconProps={ChevronLeft} onClick={props.onPageBefore} disabled={isDisabledLeft}/>
      <span className="fs20 mr20 ml20 fg5a">page {props.pageNumber}</span>
      <DefaultButton iconProps={ChevronRight} onClick={props.onPageNext}/>
    </div>
  )
}
