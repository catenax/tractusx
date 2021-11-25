import React from 'react';
import './App.scss';
import Dashboard from './components/Dashboard/Dashboard';

function App() {
  return (
    <div className="app">
      <header className="app__header">
        DevOps Tooling
      </header>
      <Dashboard />
  </div>
  );
}

export default App;
