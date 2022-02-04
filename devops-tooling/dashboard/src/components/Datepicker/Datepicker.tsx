import AdapterDateFns from '@mui/lab/AdapterDateFns';
import LocalizationProvider from '@mui/lab/LocalizationProvider';
import DatePicker from '@mui/lab/DatePicker';
import TextField from '@mui/material/TextField';

interface DatePickerProps {
  title?:string,
  value:Date|null,
  minDate?:Date|null,
  maxDate?:Date|null,
  setValue:Function,
  fullWidth?:boolean
}

export default function Datepicker({
  title, value, minDate, maxDate, setValue, fullWidth = false,
}: DatePickerProps) {
  return (
    <LocalizationProvider dateAdapter={AdapterDateFns}>
      <DatePicker
        label={title}
        value={value}
        minDate={minDate}
        maxDate={maxDate}
        onChange={(newValue) => { setValue(newValue); }}
        renderInput={(params) => <TextField fullWidth={fullWidth} {...params} />}
        views={['year', 'month', 'day']}
      />
    </LocalizationProvider>
  );
}
