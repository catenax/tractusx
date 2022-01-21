import * as React from 'react';
import CssBaseline from '@mui/material/CssBaseline';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import useAuth from '../../Auth/useAuth';
import { useLocation, useNavigate } from 'react-router-dom';
import { useState } from 'react';
import theme from '../../Theme';
import Grid from '@mui/material/Grid';
import Link from '@mui/material/Link';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Header from '../../components/Header/Header';

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
    <Container component="main" maxWidth="md" data-testid="login" sx={{mt:theme.spacing(4)}}>
      <CssBaseline />
      <Header></Header>

      <Typography sx={{textAlign:'center',mb:4}} >
           Lorem ipsum, dolor sit amet consectetur adipisicing elit. Enim quod consectetur quidem cupiditate molestiae sequi aut officia autem inventore ullam corporis asperiores corrupti porro, architecto et distinctio sunt dignissimos optio.
      </Typography>
      <Container component="main" maxWidth="sm" data-testid="login">
        <Box
          sx={{
            mt: 8,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'left',
          }}
        >

          <Typography component="h3" variant="h3"  sx={{ mb: 3}}>
          Sign In
          </Typography>
          <Box component="form" onSubmit={handleSubmit}>
            <Typography>
            Email / Username
            </Typography>
            <TextField
              value={values.username}
              margin="normal"
              fullWidth
              id="username"
              placeholder="John@email.com"
              name="username"
              autoComplete="username"
              autoFocus
              onChange={handleInputChange}
              onClick={() => resetForm('username')}
              error={errors.username?.length > 0}
              helperText={errors.username}
              inputProps={{"data-testid": "username"}}
            />
            <Typography sx={{mt:2}}>
            Password
            </Typography>
            <TextField
              value={values.password}
              margin="normal"
              fullWidth
              name="password"
              placeholder="••••••••••"
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

            <Grid container spacing={2} sx={{mt:theme.spacing(2)}}>
              <Grid item xs={6}>
                <Link > Forgot Password?</Link>
              </Grid>
              <Grid item xs={6}>
                <Button
                  type="submit"
                  fullWidth
                  variant="contained"

                  size="large"

                >
            LOGIN
                </Button>
              </Grid>
            </Grid>

          </Box>
        </Box>
      </Container>
    </Container>
  );
}
