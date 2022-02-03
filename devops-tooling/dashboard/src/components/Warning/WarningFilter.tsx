import Button from '@mui/material/Button';
import Grid from '@mui/material/Grid';
import MenuItem from '@mui/material/MenuItem';
import Select from '@mui/material/Select';
import Typography from '@mui/material/Typography';
import DeleteIcon from '@mui/icons-material/Delete';
import { useState } from 'react';
import Datepicker from '../Datepicker/Datepicker';
import theme from '../../Theme';

export default function WarningFilter() {
  const [category] = useState('ALL');
  const [fromDate, setFromDate] = useState(null);
  const [toDate, setToDate] = useState(null);

  return (
    <Grid container alignItems="center" spacing={1} sx={{ mb: theme.spacing(8) }}>
      <Grid item xs={2}>
        <Typography sx={{ fontWeight: 'bold' }}>
          From
        </Typography>
        <Datepicker value={fromDate} setValue={(newDate) => setFromDate(newDate)} />
      </Grid>
      <Grid item xs={2}>
        <Typography sx={{ fontWeight: 'bold' }}>
          To
        </Typography>
        <Datepicker value={toDate} setValue={(newDate) => setToDate(newDate)} />
      </Grid>
      <Grid item xs={2}>
        <Typography sx={{ fontWeight: 'bold' }}>
          Category
        </Typography>
        <Select
          labelId="demo-simple-select-label"
          id="demo-simple-select"
          fullWidth
          value={category}
        >
          <MenuItem value="ALL">ALL</MenuItem>
          <MenuItem value={20}>Some value</MenuItem>
          <MenuItem value={30}>Some other value</MenuItem>
        </Select>
      </Grid>
      <Grid item xs={2}>
        <Typography sx={{ fontWeight: 'bold' }}>
          Severity
        </Typography>
        <Select
          labelId="demo-simple-select-label"
          id="demo-simple-select"
          fullWidth
          value={category}
        >
          <MenuItem value="ALL">HIGH</MenuItem>
          <MenuItem value={20}>Some value</MenuItem>
          <MenuItem value={30}>Some other value</MenuItem>
        </Select>
      </Grid>
      <Grid item xs={2}>
        <Button variant="contained" color="error" startIcon={<DeleteIcon />}>
          Delete All
        </Button>
      </Grid>
    </Grid>
  );
}
