import "./Dashboard.css";

function Dashboard({ user, accounts }) {
  return (
    <div className="dashboard-card">
      <h2>Account Overview</h2>
      <div className="dashboard-row">
        <span>Name</span>
        <span>{user.name}</span>
      </div>
      <div className="dashboard-row">
        <span>Email</span>
        <span>{user.email}</span>
      </div>
      {accounts && accounts.length > 0 ? (
        accounts.map((account) => (
          <div className="dashboard-row" key={account.accountId}>
            <span>
              {account.type} #{account.accountId}
            </span>
            <span>{account.balance}</span>
          </div>
        ))
      ) : (
        <p>No accounts found.</p>
      )}
    </div>
  );
}

export default Dashboard;
