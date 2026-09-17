import { useState } from "react";
import { createUser, createAccount } from "../api";
import "./Forms.css";

function CreateAccount({ onAccountCreated, onNavigateToLogin }) {
  const [userId, setUserId] = useState("");
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [accountType, setAccountType] = useState("CHECKING");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    setLoading(true);
    try {
      const user = await createUser(userId, name, email);
      const account = await createAccount(userId, accountType);
      onAccountCreated(user, account);
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
        <label htmlFor="userId">User ID</label>
        <input
          id="userId"
          type="number"
          value={userId}
          onChange={(e) => setUserId(e.target.value)}
          required
        />

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

        <label htmlFor="accountType">Account Type</label>
        <select
          id="accountType"
          value={accountType}
          onChange={(e) => setAccountType(e.target.value)}
        >
          <option value="CHECKING">Checking</option>
          <option value="SAVING">Saving</option>
        </select>

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
