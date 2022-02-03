import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import HelpIcon from '@mui/icons-material/Help';
import Link from '@mui/material/Link';
import { Link as RouterLink } from 'react-router-dom';
import theme from '../../Theme';

export default function NotFound() {
  return (

    <Container component="main" maxWidth="xs">
      <Box
        sx={{
          marginTop: 8,
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
        }}
      >

        <HelpIcon sx={{ fontSize: 100 }} />

        <Typography component="h2" variant="h2">
          404   Not found
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

    </Container>

  );
}
