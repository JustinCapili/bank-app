import { useState } from "react";
import { createUser } from "../api";
import "./Forms.css";

function CreateAccount({ onUserCreated, onNavigateToLogin }) {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    setLoading(true);
    try {
      const user = await createUser(name, email);
      onUserCreated(user);
    } catch (err) {
      setError(
        "Could not create account. Please check your details and try again.",
      );
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="form-card">
      <h2>Create Account</h2>
      <form onSubmit={handleSubmit}>
        <label htmlFor="name">Full Name</label>
        <input
          id="name"
          type="text"
          value={name}
          onChange={(e) => setName(e.target.value)}
          required
        />

        <label htmlFor="email">Email</label>
        <input
          id="email"
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />

        {error && <p className="form-error">{error}</p>}
        <button type="submit" disabled={loading}>
          {loading ? "Creating..." : "Create Account"}
        </button>
      </form>
      <p className="form-footer">
        Already have an account?{" "}
        <button className="link-button" onClick={onNavigateToLogin}>
          Log in
        </button>
      </p>
    </div>
  );
}

export default CreateAccount;
