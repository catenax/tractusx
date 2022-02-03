import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import ErrorOutlineOutlinedIcon from '@mui/icons-material/WarningAmber';
import Link from '@mui/material/Link';
import { Link as RouterLink } from 'react-router-dom';
import theme from '../../Theme';

export default function Unauthorized() {
  return (
    <Box
      sx={{
        marginTop: theme.spacing(8),
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
      }}
    >

      <ErrorOutlineOutlinedIcon sx={{ fontSize: 100 }} />

      <Typography component="h2" variant="h2">
        Not Allowed
      </Typography>

      <Typography component="h5" variant="h5">
        <Link
          component={RouterLink}
          to="/dashboard"
          variant="body1"
          color={theme.palette.common.black}
          underline="always"
          sx={{ ml: theme.spacing(4), mr: theme.spacing(4) }}
        >
          Go to home
        </Link>
      </Typography>
    </Box>
  );
}
