import { useState } from "react";
import { getUser } from "../api";
import "./Forms.css";

function Login({ onLoginSuccess, onNavigateToCreate }) {
  const [userId, setUserId] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    setLoading(true);
    try {
      const user = await getUser(userId);
      onLoginSuccess(user);
    } catch (err) {
      setError("No account found with that User ID.");
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
