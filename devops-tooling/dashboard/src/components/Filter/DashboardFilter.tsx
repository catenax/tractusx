import Button from "@mui/material/Button";
import Grid from "@mui/material/Grid";
import TextField from "@mui/material/TextField";
import { useState } from "react";
import useAuth from "../../Auth/useAuth";
import Datepicker from "../Datepicker/Datepicker";


export default function DashboardFilter(props) {
  const auth = useAuth();

  const [filterStartDate, setFilterStartDate] = useState(null);
  const [filterEndDate, setFilterEndDate] = useState(null);
  const [minDate, setMinDate] = useState(null);
  const [maxDate, setMaxDate] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');

  const onFilter = () => {
    props.onFilter(filterStartDate, filterEndDate, searchTerm);
  }

  const onStartDateChange = (value) => {
    setMinDate(value);
    setFilterStartDate(value);
  }
  const onEndDateChange = (value) => {
    setMaxDate(value);
    setFilterEndDate(value);
  }

  const handleSearchChange = (e) => {
    setSearchTerm(e.target.value)
  }

  const reset = () => {
    setSearchTerm('');
    setMinDate(null);
    setFilterStartDate(null);
    setMaxDate(null);
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
    {auth.user==="admin" &&
      <>
        <Grid item xs={3}>
          <Datepicker title="Start Date" maxDate={maxDate} setValue={onStartDateChange} value={filterStartDate}></Datepicker>
        </Grid>
        <Grid item xs={3}>
          <Datepicker title="End Date" minDate={minDate} setValue={onEndDateChange} value={filterEndDate}></Datepicker>
        </Grid>
      </>
    }
    <Grid item xs={1}>
      <Button variant="contained" color="primary" onClick={onFilter} >Search</Button>
    </Grid>
    <Grid item xs={1}>
      <Button variant="contained" color="primary" onClick={reset}>Reset</Button>
    </Grid>
  </Grid>)
}
