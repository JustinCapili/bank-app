import "./Header.css";

function Header({ user, view, onNavigate, onLogout }) {
  return (
    <header className="app-header">
      <div className="app-header__brand">Simple Bank</div>
      <nav className="app-header__nav">
        {user ? (
          <>
            <span className="app-header__user">Welcome, {user.name}</span>
            <button onClick={onLogout}>Log Out</button>
          </>
        ) : (
          <>
            <button
              className={view === "login" ? "active" : ""}
              onClick={() => onNavigate("login")}
            >
              Log In
            </button>
            <button
              className={view === "create" ? "active" : ""}
              onClick={() => onNavigate("create")}
            >
              Create Account
            </button>
          </>
        )}
      </nav>
    </header>
  );
}

export default Header;
