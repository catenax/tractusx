import { Link, Typography } from "@mui/material";
import theme from "../../Theme";

export default function DescriptionList(props){
  return (
    <div style={{marginBottom: theme.spacing(1)}}>
      <Typography component={'span'} sx={{fontWeight: 'bold'}}>{props.topic}: </Typography>
      {props.link ?
        <Link href={props.link}>{props.link}</Link> :
        <Typography component={'span'}>{props.description}</Typography>
      }
    </div>
  )
}
