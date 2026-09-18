import { useState } from "react";
import { depositToAccount, withdrawFromAccount } from "../api";
import "./Dashboard.css";

function Dashboard({ user, accounts, onCreateAccount, onAccountsChanged }) {
  const [accountType, setAccountType] = useState("CHECKING");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const [amounts, setAmounts] = useState({});
  const [transactionError, setTransactionError] = useState("");
  const [pendingAccountId, setPendingAccountId] = useState(null);

  const accountIcons = { CHECKING: "💳", SAVINGS: "🏦" };

  async function handleCreateAccount(e) {
    e.preventDefault();
    setError("");
    setLoading(true);
    try {
      await onCreateAccount(accountType);
    } catch (err) {
      setError("Could not create account. Please try again.");
    } finally {
      setLoading(false);
    }
  }

  function handleAmountChange(accountId, value) {
    setAmounts((prev) => ({ ...prev, [accountId]: value }));
  }

  async function handleDeposit(accountId) {
    setTransactionError("");
    setPendingAccountId(accountId);
    try {
      await depositToAccount(accountId, amounts[accountId]);
      await onAccountsChanged();
    } catch (err) {
      setTransactionError("Could not deposit. Please check the amount.");
    } finally {
      setPendingAccountId(null);
    }
  }

  async function handleWithdraw(accountId) {
    setTransactionError("");
    setPendingAccountId(accountId);
    try {
      await withdrawFromAccount(accountId, amounts[accountId]);
      await onAccountsChanged();
    } catch (err) {
      setTransactionError("Could not withdraw. Please check the amount.");
    } finally {
      setPendingAccountId(null);
    }
  }

  return (
    <div className="dashboard-card">
      <h2>Account Overview</h2>
      <div className="dashboard-row">
        <span>User ID</span>
        <span>{user.userId}</span>
      </div>
      <div className="dashboard-row">
        <span>Name</span>
        <span>{user.name}</span>
      </div>
      <div className="dashboard-row">
        <span>Email</span>
        <span>{user.email}</span>
      </div>
      {transactionError && <p className="form-error">{transactionError}</p>}
      {accounts && accounts.length > 0 ? (
        accounts.map((account) => (
          <div className="dashboard-account" key={account.accountId}>
            <div className="dashboard-row">
              <span className="dashboard-account__label">
                <span className="dashboard-account__icon" aria-hidden="true">
                  {accountIcons[account.type] || "💰"}
                </span>
                {account.type} #{account.accountId}
              </span>
              <span className="dashboard-account__balance">
                {account.balance}
              </span>
            </div>
            <div className="dashboard-account__actions">
              <input
                type="number"
                min="0"
                step="0.01"
                placeholder="Amount"
                value={amounts[account.accountId] || ""}
                onChange={(e) =>
                  handleAmountChange(account.accountId, e.target.value)
                }
              />
              <button
                type="button"
                disabled={pendingAccountId === account.accountId}
                onClick={() => handleDeposit(account.accountId)}
              >
                Deposit
              </button>
              <button
                type="button"
                disabled={pendingAccountId === account.accountId}
                onClick={() => handleWithdraw(account.accountId)}
              >
                Withdraw
              </button>
            </div>
          </div>
        ))
      ) : (
        <div className="dashboard-empty">
          <span className="dashboard-empty__icon" aria-hidden="true">
            📭
          </span>
          <p>No accounts yet. Open your first one below to get started.</p>
        </div>
      )}

      <form className="dashboard-create-account" onSubmit={handleCreateAccount}>
        <label htmlFor="accountType">New Account Type</label>
        <select
          id="accountType"
          value={accountType}
          onChange={(e) => setAccountType(e.target.value)}
        >
          <option value="CHECKING">Checking</option>
          <option value="SAVINGS">Savings</option>
        </select>
        {error && <p className="form-error">{error}</p>}
        <button type="submit" disabled={loading}>
          {loading ? "Creating..." : "Create Account"}
        </button>
      </form>
    </div>
  );
}

export default Dashboard;
