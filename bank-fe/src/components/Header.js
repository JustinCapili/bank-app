import { Link, NavLink } from "react-router-dom";
import "./Header.css";

function Header({ user, onLogout }) {
  return (
    <header className="app-header">
      <Link to="/" className="app-header__brand">
        Simple Bank
      </Link>
      <nav className="app-header__nav">
        {user ? (
          <>
            <NavLink
              to="/dashboard"
              className={({ isActive }) => (isActive ? "active" : "")}
            >
              Dashboard
            </NavLink>
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
              className={({ isActive }) => (isActive ? " active" : "")}
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
