import { useState } from "react";
import { Navigate, Route, Routes, useNavigate } from "react-router-dom";
import "./App.css";
import Header from "./components/Header";
import Login from "./components/Login";
import CreateAccount from "./components/CreateAccount";
import Dashboard from "./components/Dashboard";
import { getAccountsByUser } from "./api";

function App() {
  const [user, setUser] = useState(null);
  const [accounts, setAccounts] = useState([]);
  const navigate = useNavigate();

  async function loadAccounts(userId) {
    try {
      setAccounts(await getAccountsByUser(userId));
    } catch (err) {
      setAccounts([]);
    }
  }

  async function handleLoginSuccess(loggedInUser) {
    setUser(loggedInUser);
    await loadAccounts(loggedInUser.userId);
    navigate("/dashboard");
  }

  async function handleAccountCreated(newUser) {
    setUser(newUser);
    await loadAccounts(newUser.userId);
    navigate("/dashboard");
  }

  function handleLogout() {
    setUser(null);
    setAccounts([]);
    navigate("/login");
  }

  return (
    <div className="App">
      <Header user={user} onLogout={handleLogout} />
      <main className="App-main">
        <Routes>
          <Route path="/" element={<Navigate to="/login" replace />} />
          <Route
            path="/login"
            element={
              <Login
                onLoginSuccess={handleLoginSuccess}
                onNavigateToCreate={() => navigate("/create")}
              />
            }
          />
          <Route
            path="/create"
            element={
              <CreateAccount
                onAccountCreated={handleAccountCreated}
                onNavigateToLogin={() => navigate("/login")}
              />
            }
          />
          <Route
            path="/dashboard"
            element={
              user ? (
                <Dashboard user={user} accounts={accounts} />
              ) : (
                <Navigate to="/login" replace />
              )
            }
          />
        </Routes>
      </main>
    </div>
  );
}

export default App;
