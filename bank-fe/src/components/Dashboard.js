import "./Dashboard.css";

function Dashboard({ user, account }) {
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
      {account && (
        <>
          <div className="dashboard-row">
            <span>Account Type</span>
            <span>{account.accountType}</span>
          </div>
          <div className="dashboard-row">
            <span>Account ID</span>
            <span>{account.id}</span>
          </div>
          <div className="dashboard-row">
            <span>Balance</span>
            <span>{account.balance}</span>
          </div>
        </>
      )}
    </div>
  );
}

export default Dashboard;
