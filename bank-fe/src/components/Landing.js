import { useNavigate } from "react-router-dom";
import "./Landing.css";

function Landing({ user }) {
  const navigate = useNavigate();

  return (
    <div className="landing">
      <section className="landing-hero">
        <span className="landing-hero__badge">Simple Bank</span>
        <h1>Banking that stays out of your way.</h1>
        <p>
          Open an account in seconds, track every balance in real time, and move
          money with confidence — all from one clean dashboard.
        </p>
        <div className="landing-hero__actions">
          {user ? (
            <button
              className="btn btn-primary"
              onClick={() => navigate("/dashboard")}
            >
              Go to Dashboard
            </button>
          ) : (
            <>
              <button
                className="btn btn-primary"
                onClick={() => navigate("/create")}
              >
                Get Started
              </button>
              <button
                className="btn btn-ghost"
                onClick={() => navigate("/login")}
              >
                Log In
              </button>
            </>
          )}
        </div>
      </section>

      <section className="landing-features">
        <div className="landing-feature">
          <div className="landing-feature__icon">💳</div>
          <h3>Checking &amp; Savings</h3>
          <p>Open multiple account types and manage them all in one place.</p>
        </div>
        <div className="landing-feature">
          <div className="landing-feature__icon">⚡</div>
          <h3>Instant Updates</h3>
          <p>Deposits and withdrawals reflect on your dashboard immediately.</p>
        </div>
        <div className="landing-feature">
          <div className="landing-feature__icon">🔒</div>
          <h3>Secure by Design</h3>
          <p>Your session is protected with token-based authentication.</p>
        </div>
      </section>
    </div>
  );
}

export default Landing;
