import { useState } from 'react';
import { Grid, TextField, Button } from '@mui/material';
import { isBefore } from 'date-fns';
import useAuth from '../../Auth/useAuth';
import Datepicker from '../Datepicker/Datepicker';

const todaysDate = new Date();

export default function DashboardFilter({ onFilter: onFilterProp }) {
  const auth = useAuth();

  const [filterStartDate, setFilterStartDate] = useState(null);
  const [filterEndDate, setFilterEndDate] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');

  const maxEndDate = new Date();
  const maxStartDate = (filterEndDate && isBefore(filterEndDate, todaysDate))
    ? filterEndDate
    : todaysDate;
  const onFilter = () => {
    onFilterProp(filterStartDate, filterEndDate, searchTerm);
  };

  const onStartDateChange = (value) => {
    setFilterStartDate(value);
  };
  const onEndDateChange = (value) => {
    setFilterEndDate(value);
  };

  const handleSearchChange = (e) => {
    setSearchTerm(e.target.value);
  };

  const reset = () => {
    setSearchTerm('');
    setFilterStartDate(null);
    setFilterEndDate(null);
    onFilterProp(null, null, '');
  };

  return (
    <Grid container spacing={1} sx={{ mb: 4 }} alignItems="center">
      <Grid item xs={4}>
        <TextField
          label="Search Connector"
          variant="outlined"
          fullWidth
          value={searchTerm}
          onChange={handleSearchChange}
          inputProps={{ 'data-testid': 'searchText' }}
        />
      </Grid>
      {auth.isAdmin()
      && (
      <>
        <Grid item xs={3}>
          <Datepicker fullWidth title="Start Date" maxDate={maxStartDate} setValue={onStartDateChange} value={filterStartDate} />
        </Grid>
        <Grid item xs={3}>
          <Datepicker fullWidth title="End Date" minDate={filterStartDate} maxDate={maxEndDate} setValue={onEndDateChange} value={filterEndDate} />
        </Grid>
      </>
      )}
      <Grid item xs={1}>
        <Button fullWidth variant="contained" color="primary" onClick={onFilter}>Search</Button>
      </Grid>
      <Grid item xs={1}>
        <Button fullWidth variant="contained" color="primary" onClick={reset}>Reset</Button>
      </Grid>
    </Grid>
  );
}
