import React from 'react';
import Dashboard from './components/Dashboard/Dashboard';
import ThemeProvider from '@mui/system/ThemeProvider'
import Grid from '@mui/material/Grid'
import Button from '@mui/material/Button'
import theme from './Theme';

function App() {
  return (
    <ThemeProvider theme={theme}>
      <Grid container direction="column">
        <Grid container justifyContent="center" sx={{p: 2}}>
          <span>DevOps Tooling</span>
          <Button variant="contained" color="primary">Just a button</Button>
        </Grid>
        <Dashboard />
      </Grid>
    </ThemeProvider >
  );
}

export default App;
