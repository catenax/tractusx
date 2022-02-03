// GENERAL DOC: https://mui.com/customization/theming/

import { createTheme } from '@mui/material/styles';

const theme = createTheme({
  palette: {
    primary: {
      main: '#b3cb2d',
    },
    secondary: {
      main: '#ffa600',
    },
  },
  typography: {
    subtitle1: {
      fontSize: 16,
      color: '#777',
    },
  },
});

export default theme;
