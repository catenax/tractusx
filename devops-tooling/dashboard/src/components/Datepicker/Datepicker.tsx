import React from "react";
import AdapterDateFns from '@mui/lab/AdapterDateFns';
import LocalizationProvider from '@mui/lab/LocalizationProvider';
import DatePicker from '@mui/lab/DatePicker';
import TextField from '@mui/material/TextField';


export default function Datepicker(props){
  return (
    <LocalizationProvider dateAdapter={AdapterDateFns}>
      <DatePicker
        label={props.title}
        value={props.value}
        minDate={props.minDate}
        maxDate={props.maxDate}
        onChange={(newValue) => {props.setValue(newValue);}}
        renderInput={(params) => <TextField fullWidth={props.fullWidth} {...params} />}
        views={["year","month","day"]}
      />
    </LocalizationProvider>
  )

}
