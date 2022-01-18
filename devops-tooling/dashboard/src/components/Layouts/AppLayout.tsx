import { Box, Button, Link  } from "@mui/material";
import { Outlet } from "react-router-dom";
import useAuth from '../../Auth/useAuth';
import theme from "../../Theme";
import Header from "../Header/Header";

export default function AppLayout() {
  const navi = ['Connector Landscape', 'Tools', 'Warnings', 'FAQ', 'Configuration']
  const auth = useAuth();

  const handleLogoutClick = () => {
    auth.signOut(()=>console.log("logging out"))
  }

  return (
    <>
      <Box sx={{ display: 'flex',
        flexDirection: 'column',
        minHeight: '100vh',
        padding: `${theme.spacing(4)} ${theme.spacing(8)}`}}>
        <Button onClick={handleLogoutClick} color="inherit" sx={{position: 'absolute', right: theme.spacing(8)}}>Logout</Button>
        <Header></Header>
        <Box sx={{ display: 'flex', justifyContent: 'center'}}>
          {navi.map(item =>
            <Link href="#" variant="body1" color='black' underline="hover" sx={{ml: theme.spacing(4), mr: theme.spacing(4)}}>{item}</Link>
          )}
        </Box>
        <Box component="main" sx={{flexGrow: 1, mt: theme.spacing(4)}}>
          <Outlet />
        </Box>
      </Box>
    </>
  )
}
