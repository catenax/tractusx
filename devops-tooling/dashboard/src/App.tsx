import React from 'react';
import ThemeProvider from '@mui/system/ThemeProvider'
import theme from './Theme';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Login from './pages/Login/Login';
import RequireAuth from './Auth/RequireAuth';
import AuthProvider from './Auth/AuthProvider';
import Dashboard from './pages/Dashboard/Dashboard';
import AppLayout from './components/Layouts/AppLayout';

function App() {
  return (
    <ThemeProvider theme={theme}>
      <BrowserRouter>
        <AuthProvider>
          <Routes>
            <Route path="/" element={
              <Login />
            } />
            <Route element={<AppLayout/>} >
              <Route path="dashboard" element={
                <RequireAuth><Dashboard /></RequireAuth>
              }   />
            </Route>
          </Routes>
        </AuthProvider>
      </BrowserRouter>
    </ThemeProvider >
  );
}

export default App;
