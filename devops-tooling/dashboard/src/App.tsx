import React from 'react';
import ThemeProvider from '@mui/system/ThemeProvider'
import theme from './Theme';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Login from './components/pages/Login/Login';
import RequireAuth from './Auth/RequireAuth';
import AuthProvider from './Auth/AuthProvider';
import Dashboard from './components/pages/Dashboard/Dashboard';

function App() {
  return (
    <ThemeProvider theme={theme}>
      <BrowserRouter>
        <AuthProvider>
          <Routes>
            <Route path="/" element={
              <Login />
            } />
            <Route path="dashboard" element={
              <RequireAuth><Dashboard /></RequireAuth>
            }   />
          </Routes>
        </AuthProvider>
      </BrowserRouter>
    </ThemeProvider >
  );
}

export default App;
