import React, { useState } from "react";

const LoginPage = () => {
  const [isRegistering, setIsRegistering] = useState(false); // Toggle between login and register
  const [loginForm, setLoginForm] = useState({ username: "", password: "" });
  const [registerForm, setRegisterForm] = useState({
    username: "",
    password: "",
    email: "",
  });

  const [message, setMessage] = useState("");

  const handleLoginSubmit = (e) => {
    e.preventDefault();
    fetch("http://localhost:8080/api/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
      body: JSON.stringify(loginForm),
    })
      .then((response) => {
        if (response.ok) {
          setMessage("Login successful!");
          // Redirect to a new page (e.g., dashboard)
          window.location.href = "/dashboard";
        } else {
          setMessage("Login failed. Please check your username or password.");
        }
      })
      .catch((error) => {
        setMessage("An error occurred. Please try again later.");
        console.error("Error:", error);
      });
  };

  const handleRegisterSubmit = (e) => {
    e.preventDefault();
    fetch("http://localhost:8080/api/register", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(registerForm),
    })
      .then((response) => {
        if (response.ok) {
          setMessage("Registration successful! You can now log in.");
          setIsRegistering(false); // Switch to login view
        } else {
          setMessage("Registration failed. Please try again.");
        }
      })
      .catch((error) => {
        setMessage("An error occurred. Please try again later.");
        console.error("Error:", error);
      });
  };

  return (
    <div style={{ maxWidth: "400px", margin: "0 auto", padding: "20px" }}>
      <h2>{isRegistering ? "Register" : "Login"}</h2>
      <form onSubmit={isRegistering ? handleRegisterSubmit : handleLoginSubmit}>
        {isRegistering ? (
          <>
            <div>
              <label>
                Username:
                <input
                  type="text"
                  value={registerForm.username}
                  onChange={(e) =>
                    setRegisterForm({ ...registerForm, username: e.target.value })
                  }
                  required
                />
              </label>
            </div>
            <div>
              <label>
                Password:
                <input
                  type="password"
                  value={registerForm.password}
                  onChange={(e) =>
                    setRegisterForm({ ...registerForm, password: e.target.value })
                  }
                  required
                />
              </label>
            </div>
            <div>
              <label>
                Email:
                <input
                  type="email"
                  value={registerForm.email}
                  onChange={(e) =>
                    setRegisterForm({ ...registerForm, email: e.target.value })
                  }
                  required
                />
              </label>
            </div>
          </>
        ) : (
          <>
            <div>
              <label>
                Username:
                <input
                  type="text"
                  value={loginForm.username}
                  onChange={(e) =>
                    setLoginForm({ ...loginForm, username: e.target.value })
                  }
                  required
                />
              </label>
            </div>
            <div>
              <label>
                Password:
                <input
                  type="password"
                  value={loginForm.password}
                  onChange={(e) =>
                    setLoginForm({ ...loginForm, password: e.target.value })
                  }
                  required
                />
              </label>
            </div>
          </>
        )}
        <div style={{ marginTop: "10px" }}>
          <button type="submit">{isRegistering ? "Register" : "Login"}</button>
        </div>
      </form>
      <div style={{ marginTop: "10px" }}>
        <p style={{ color: "red" }}>{message}</p>
        <button onClick={() => setIsRegistering(!isRegistering)}>
          {isRegistering ? "Already have an account? Login" : "Don't have an account? Register"}
        </button>
      </div>
    </div>
  );
};

export default LoginPage;
