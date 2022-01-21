import { Box, Link } from "@mui/material"
import theme from "../../Theme"

export default function Navigation(){
  const navi = ['Connector Landscape', 'Tools', 'Warnings', 'FAQ', 'Configuration']
  return (
    <Box sx={{ display: 'flex', justifyContent: 'center'}}>
      {navi.map(item =>
        <Link href="#" variant="body1" color='black' underline="hover" sx={{ml: theme.spacing(4), mr: theme.spacing(4)}}>{item}</Link>
      )}
    </Box>
  )
}
