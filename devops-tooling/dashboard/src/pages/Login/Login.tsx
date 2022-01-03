import * as React from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
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
  const [errors, setErrors] = useState({...defaultErrors});

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (!validate()) return;

    if (loginDataIsValid()) {
      auth.signIn(values.username, () => navigate(from, { replace: true }));
    } else {
      setErrors({...errors, 'login': 'Authentication failed. Please try again!'})
    }
  };

  const loginDataIsValid = () => {
    return staticUsers.filter(user => JSON.stringify(user) === JSON.stringify(values)).length > 0;
  }

  const handleInputChange = e => {
    const { name, value } = e.target;
    setValues({...values, [name]: value});
    // after user entered the text, we need to clear the error
    setErrors({...errors,  [name]: ''});
  }

  const validate = () => {
    let isFormValid = true;
    const temp = {...errors};

    if (values.username === '') {
      isFormValid = false;
      temp.username = required;
    }

    if (values.password === '') {
      isFormValid = false;
      temp.password = required;
    }
    setErrors({...temp});

    return isFormValid;
  }

  const resetForm = (name) => {
    setErrors({...defaultErrors});
  }


  return (
    <Container component="main" maxWidth="xs" data-testid="login">
      <CssBaseline />
      <Box
        sx={{
          mt: 8,
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
            error={errors.username?.length > 0}
            helperText={errors.username}
            inputProps={{"data-testid": "username"}}
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
            error={errors.password?.length > 0}
            helperText={errors.password}
            inputProps={{"data-testid": "password"}}
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
  );
}