import { useState } from "react";
import "./App.css";
import Header from "./components/Header";
import Login from "./components/Login";
import CreateAccount from "./components/CreateAccount";
import Dashboard from "./components/Dashboard";
import { getAccountsByUser } from "./api";

function App() {
  const [view, setView] = useState("login");
  const [user, setUser] = useState(null);
  const [accounts, setAccounts] = useState([]);

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
    setView("dashboard");
  }

  async function handleAccountCreated(newUser) {
    setUser(newUser);
    await loadAccounts(newUser.userId);
    setView("dashboard");
  }

  function handleLogout() {
    setUser(null);
    setAccounts([]);
    setView("login");
  }

  return (
    <div className="App">
      <Header
        user={user}
        view={view}
        onNavigate={setView}
        onLogout={handleLogout}
      />
      <main className="App-main">
        {view === "login" && (
          <Login
            onLoginSuccess={handleLoginSuccess}
            onNavigateToCreate={() => setView("create")}
          />
        )}
        {view === "create" && (
          <CreateAccount
            onAccountCreated={handleAccountCreated}
            onNavigateToLogin={() => setView("login")}
          />
        )}
        {view === "dashboard" && user && (
          <Dashboard user={user} accounts={accounts} />
        )}
      </main>
    </div>
  );
}

export default App;
