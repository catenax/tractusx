import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import DeleteIcon from '@mui/icons-material/Delete';
import theme from '../../Theme';

export default function WarningListItem() {
  return (
    <Card variant="outlined" sx={{ width: '80%', mb: theme.spacing(4) }}>
      <CardContent>
        <Grid container direction="row" alignItems="center" spacing={2}>
          <Grid item xs={10}>
            <Box>
              <Typography variant="h4" component="div" noWrap>
                WarningName
              </Typography>
              <Typography variant="body2" sx={{ color: 'text.secondary' }} noWrap>
                Lorem ipsum dolor sit amet consectetur adipisicing elit.
                Molestias quod, architecto, exercitationem explicabo quidem error corporis alias.
              </Typography>
            </Box>
          </Grid>
          <Grid item xs={2}>
            <Button color="error" startIcon={<DeleteIcon />}>
              Delete
            </Button>
          </Grid>
        </Grid>
      </CardContent>
    </Card>
  );
}
