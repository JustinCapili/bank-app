import { useNavigate } from "react-router-dom";
import "./NotFound.css";

function NotFound() {
  const navigate = useNavigate();

  return (
    <div className="not-found">
      <span className="not-found__code">404</span>
      <h1>We couldn't find that page.</h1>
      <p>The page you're looking for doesn't exist or may have moved.</p>
      <button className="btn btn-primary" onClick={() => navigate("/")}>
        Back to Home
      </button>
    </div>
  );
}

export default NotFound;
