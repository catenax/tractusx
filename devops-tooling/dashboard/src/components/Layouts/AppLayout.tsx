import { Box, CssBaseline,Drawer,AppBar,Toolbar,List,Typography,Divider,ListItem ,ListItemIcon ,ListItemText,Grid,Button  } from "@mui/material";
import { Outlet } from "react-router-dom";
import InboxIcon from '@mui/icons-material/MoveToInbox';
import DashboardIcon from '@mui/icons-material/Dashboard';
import MailIcon from '@mui/icons-material/Mail';
import useAuth from '../../Auth/useAuth';
import theme from "../../Theme";
const drawerWidth = 240;

export default function AppLayout() {

  const auth = useAuth();

  const handleLogoutClick= () => {
    auth.signOut(()=>console.log("logging out"))
  }


  return (
    <>
      <Box sx={{ display: 'flex', minHeight: '100vh' }}>
        <CssBaseline />
        <AppBar position="fixed" sx={{ zIndex: (theme) => theme.zIndex.drawer + 1 }}>
          <Toolbar>
            <Grid container direction="row" justifyContent="space-between" alignItems="center">
              <Typography variant="h6" noWrap component="div"  >
                Catena-X
              </Typography>
              <Button onClick={handleLogoutClick} color="inherit">Logout</Button>
            </Grid>
          </Toolbar>
        </AppBar>
        <Drawer
          variant="permanent"
          sx={{
            width: drawerWidth,
            flexShrink: 0,
            [`& .MuiDrawer-paper`]: { width: drawerWidth, boxSizing: 'border-box' },
            [`& .MuiPaper-root`]: { marginTop: `${theme.spacing(8)}` },
          }}
        >
          <Box sx={{ overflow: 'auto' }}>
            <List>
              <ListItem button  >
                <ListItemIcon>
                  <DashboardIcon />
                </ListItemIcon>
                <ListItemText>  Dashboard </ListItemText>
              </ListItem>
              <ListItem button  >
                <ListItemIcon>
                  <MailIcon />
                </ListItemIcon>
                <ListItemText>  Inactive </ListItemText>
              </ListItem>
            </List>
            <Divider />
            <List>
              <ListItem button  >
                <ListItemIcon>
                  <InboxIcon />
                </ListItemIcon>
                <ListItemText>  Inactive </ListItemText>
              </ListItem>
            </List>
          </Box>
        </Drawer>
        <Box component="main" sx={{ flexGrow: 1, p: 3, [`& .MuiGrid-container`]: { marginTop: `${theme.spacing(8)}`}, }}>
          <Outlet />
        </Box>
      </Box>
    </>
  )
}
