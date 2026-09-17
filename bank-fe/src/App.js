import { useState } from "react";
import "./App.css";
import Header from "./components/Header";
import Login from "./components/Login";
import CreateAccount from "./components/CreateAccount";
import Dashboard from "./components/Dashboard";

function App() {
  const [view, setView] = useState("login");
  const [user, setUser] = useState(null);
  const [account, setAccount] = useState(null);

  function handleLoginSuccess(loggedInUser) {
    setUser(loggedInUser);
    setView("dashboard");
  }

  function handleAccountCreated(newUser, newAccount) {
    setUser(newUser);
    setAccount(newAccount);
    setView("dashboard");
  }

  function handleLogout() {
    setUser(null);
    setAccount(null);
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
          <Dashboard user={user} account={account} />
        )}
      </main>
    </div>
  );
}

export default App;
