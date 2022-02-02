import Button from "@mui/material/Button";
import Grid from "@mui/material/Grid";
import TextField from "@mui/material/TextField";
import { useState } from "react";
import useAuth from "../../Auth/useAuth";
import Datepicker from "../Datepicker/Datepicker";
import {isBefore} from 'date-fns';

let todaysDate = new Date();

export default function DashboardFilter(props) {
  const auth = useAuth();

  const [filterStartDate, setFilterStartDate] = useState(null);
  const [filterEndDate, setFilterEndDate] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');

  let maxEndDate = new Date();
  let maxStartDate  = (filterEndDate && isBefore(filterEndDate,todaysDate)) ? filterEndDate : todaysDate;


  const onFilter = () => {
    props.onFilter(filterStartDate, filterEndDate, searchTerm);
  }

  const onStartDateChange = (value) => {
    setFilterStartDate(value);
  }
  const onEndDateChange = (value) => {
    setFilterEndDate(value);
  }

  const handleSearchChange = (e) => {
    setSearchTerm(e.target.value)
  }

  const reset = () => {
    setSearchTerm('');
    setFilterStartDate(null);
    setFilterEndDate(null);
    props.onFilter(null, null, '');
  }


  return ( <Grid container spacing={1} sx={{mb: 4}} alignItems='center'>
    <Grid item xs={4}>
      <TextField
        label="Search Connector"
        variant="outlined"
        fullWidth
        value={searchTerm}
        onChange={handleSearchChange}
        inputProps={{"data-testid":"searchText"}} />
    </Grid>
    {auth.isAdmin() &&
      <>
        <Grid item xs={3}>
          <Datepicker fullWidth title="Start Date" maxDate={maxStartDate} setValue={onStartDateChange} value={filterStartDate}></Datepicker>
        </Grid>
        <Grid item xs={3}>
          <Datepicker fullWidth title="End Date" minDate={filterStartDate} maxDate={maxEndDate} setValue={onEndDateChange} value={filterEndDate}></Datepicker>
        </Grid>
      </>
    }
    <Grid item xs={1}>
      <Button fullWidth variant="contained" color="primary" onClick={onFilter} >Search</Button>
    </Grid>
    <Grid item xs={1}>
      <Button fullWidth variant="contained" color="primary" onClick={reset}>Reset</Button>
    </Grid>
  </Grid>)
}
