import React from 'react';
import ThemeProvider from '@mui/system/ThemeProvider'
import theme from './Theme';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Login from './pages/Login/Login';
import RequireAuth from './Auth/RequireAuth';
import AuthProvider from './Auth/AuthProvider';
import Dashboard from './pages/Dashboard/Dashboard';
import AppLayout from './components/Layouts/AppLayout';
import Tools from './pages/Tools/Tools';
import NotFound from './pages/Errors/NotFound';
import Warning from './pages/Warning/Warning';
import Faq from './pages/FAQ/Faq';
import Configuration from './pages/Configuration/Configuration';

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
                <RequireAuth needAdminRights={false}><Dashboard /></RequireAuth>
              }   />
              <Route path="tools" element={
                <RequireAuth needAdminRights={true} ><Tools /></RequireAuth>
              }   />
              <Route path="warnings" element={
                <RequireAuth needAdminRights={true} ><Warning /></RequireAuth>
              }   />
              <Route path="faq" element={
                <RequireAuth needAdminRights={true} ><Faq /></RequireAuth>
              }   />
              <Route path="configuration" element={
                <RequireAuth needAdminRights={true} ><Configuration /></RequireAuth>
              }   />
            </Route>
            <Route path="*" element={<NotFound />} />
          </Routes>
        </AuthProvider>
      </BrowserRouter>
    </ThemeProvider >
  );
}

export default App;