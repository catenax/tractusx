import { Box, Button } from '@mui/material';
import { Outlet } from 'react-router-dom';
import useAuth from '../../Auth/useAuth';
import theme from '../../Theme';
import Header from '../Header/Header';
import Navigation from '../Navigation/Navigation';

export default function AppLayout() {
  const auth = useAuth();

  const handleLogoutClick = () => {
    // eslint-disable-next-line no-console
    auth.signOut(() => console.log('logging out'));
  };

  return (
    <Box sx={{
      display: 'flex',
      flexDirection: 'column',
      minHeight: '100vh',
      padding: `${theme.spacing(4)} ${theme.spacing(8)}`,
    }}
    >
      <Button onClick={handleLogoutClick} color="inherit" sx={{ position: 'absolute', right: theme.spacing(8) }}>Logout</Button>
      <Header />
      {auth.isAdmin() && <Navigation />}
      <Box component="main" sx={{ flexGrow: 1, mt: theme.spacing(4) }}>
        <Outlet />
      </Box>
    </Box>
  );
}
