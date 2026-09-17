import { render, screen } from "@testing-library/react";
import App from "./App";

test("renders login form by default", () => {
  render(<App />);
  const heading = screen.getByRole("heading", { name: /log in/i });
  expect(heading).toBeInTheDocument();
});
