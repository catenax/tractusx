import { Box, Typography  } from "@mui/material";
import theme from "../../Theme";

export default function Header(){
  return (
    <Box sx={{display: 'flex', flexDirection: 'column', alignItems: 'center', mb: theme.spacing(4)}}>
      <Typography sx={{mb: theme.spacing(1)}} variant='subtitle1' component='h1'>Catena-X</Typography>
      <Typography component='h2'>Connector Dashboard</Typography>
    </Box>
  )
}
