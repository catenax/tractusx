import Link from "@mui/material/Link";
import { useMatch, useResolvedPath } from "react-router-dom";
import theme from "../../Theme"
import { Link as RouterLink } from 'react-router-dom';

export default function NavLink(props) {
  let resolved = useResolvedPath(props.to);
  let match = useMatch({ path: resolved.pathname, end: true });

  return (
    <div>
      <Link
        component={RouterLink}
        to={props.to}
        variant="body1"
        color={theme.palette.common.black}
        underline={match ? "always" : "hover"}
        sx={{ml: theme.spacing(4), mr: theme.spacing(4)}}>
        {props.name}
      </Link>

    </div>
  );
}

