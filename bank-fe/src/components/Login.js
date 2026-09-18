import { useState } from "react";
import { login, setToken } from "../api";
import "./Forms.css";

function Login({ onLoginSuccess, onNavigateToCreate }) {
  const [userId, setUserId] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    setLoading(true);
    try {
      const { user, token } = await login(userId, password);
      setToken(token);
      onLoginSuccess(user);
    } catch (err) {
      setError("Invalid User ID or password.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="form-card">
      <h2>Log In</h2>
      <form onSubmit={handleSubmit}>
        <label htmlFor="userId">User ID</label>
        <input
          id="userId"
          type="number"
          value={userId}
          onChange={(e) => setUserId(e.target.value)}
          required
        />

        <label htmlFor="password">Password</label>
        <input
          id="password"
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        {error && <p className="form-error">{error}</p>}
        <button type="submit" disabled={loading}>
          {loading ? "Logging in..." : "Log In"}
        </button>
      </form>
      <p className="form-footer">
        Don't have an account?{" "}
        <button className="link-button" onClick={onNavigateToCreate}>
          Create one
        </button>
      </p>
    </div>
  );
}

export default Login;
