import { NavLink } from "react-router-dom";
import "./Header.css";

function Header({ user, onLogout }) {
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
            <NavLink
              to="/login"
              className={({ isActive }) => (isActive ? "active" : "")}
            >
              Log In
            </NavLink>
            <NavLink
              to="/create"
              className={({ isActive }) => (isActive ? "active" : "")}
            >
              Create Account
            </NavLink>
          </>
        )}
      </nav>
    </header>
  );
}

export default Header;
