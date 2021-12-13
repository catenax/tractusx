import React from 'react';
import Dashboard from './components/Dashboard/Dashboard';
import ThemeProvider from '@mui/system/ThemeProvider'
import Grid from '@mui/material/Grid'
import Button from '@mui/material/Button'
import theme from './Theme';

import useAuth from './Auth/useAuth'

function App() {
  const auth = useAuth();

  function handleClick () {
    auth.signOut(()=>{});
  }

  return (
    <ThemeProvider theme={theme}>
      <Grid container direction="column">
        <Grid container justifyContent="center" sx={{p: 2}}>
          <span>DevOps Tooling</span>
          <Button variant="contained" color="primary" onClick={handleClick}>Logout</Button>
        </Grid>
        <Dashboard />
      </Grid>
    </ThemeProvider >
  );
}

export default App;
