import * as React from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { ThemeProvider } from '@mui/material/styles';
import theme from '../../Theme';
import useAuth from '../../Auth/useAuth';
import { useLocation, useNavigate } from 'react-router-dom';
import { useState } from 'react';

const defaultValues = {username: '', password: ''};
const defaultErrors = {username: '', password: '', login: ''};
const staticUsers = [
  {username: 'admin', password: 'admin'},
  {username: 'user', password: 'user'}
];

export default function Login() {
  const required = "This field is required.";
  const auth = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const from = location.state?.from?.pathname || '/dashboard';
  const [values, setValues] = useState(defaultValues);
  const [errors, setErrors] = useState(defaultErrors);

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if(fieldHasError(errors.username) || fieldHasError(errors.password)) return;

    if(loginDataIsValid()) {
      auth.signin(values.username, () => navigate(from, { replace: true }));
    } else {
      setErrors({...errors, ['login']: 'Authentication failed. Please try again!'})
    }
  };

  const loginDataIsValid = () => {
    return staticUsers.filter(user => JSON.stringify(user) == JSON.stringify(values)).length > 0;
  }

  const handleInputChange = e => {
    const { name, value } = e.target;
    setValues({...values, [name]: value});
  }

  const validate = () => {
    const temp = {... errors};
    temp.username = values.username ? '' : required
    temp.password = values.password ? '' : required

    setErrors({...temp});
  }

  const resetForm = (name) => {
    setErrors(defaultErrors);
  }

  const fieldHasError = (type) => type.length > 0 || errors.login.length > 0;

  return (
    <ThemeProvider theme={theme}>
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <Box
          sx={{
            marginTop: 8,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Sign in
          </Typography>
          <Box component="form" onSubmit={handleSubmit} sx={{ mt: 1 }}>
            <TextField
              value={values.username}
              margin="normal"
              fullWidth
              id="username"
              label="Username"
              name="username"
              autoComplete="username"
              autoFocus
              onChange={handleInputChange}
              onClick={() => resetForm('username')}
              error={fieldHasError(errors.username)}
              helperText={errors.username}
            />
            <TextField
              value={values.password}
              margin="normal"
              fullWidth
              name="password"
              label="Password"
              type="password"
              id="password"
              autoComplete="current-password"
              onChange={handleInputChange}
              onClick={() => resetForm('password')}
              error={fieldHasError(errors.password)}
              helperText={errors.password}
            />
            {errors.login.length > 0 && 
              <Typography sx={{color: 'error.main'}} component="p" variant="body1">{errors.login}</Typography>
            }
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
              onMouseOver={() => validate()}
            >
              Sign In
            </Button>
          </Box>
        </Box>
      </Container>
    </ThemeProvider>
  );
}
