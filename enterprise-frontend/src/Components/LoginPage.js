import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
const LoginPage = () => {
    const [isRegistering, setIsRegistering] = useState(false);
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');
    const [feedbackMessage, setFeedbackMessage] = useState('');
    const navigate = useNavigate();
    const handleLogin = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch('http://localhost:8080/api/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ username, password }),
            });

            if (response.ok) {
                setFeedbackMessage('Login successful!');
                navigate('/books');

              } else {
                setFeedbackMessage('Login failed. Please check your credentials.');
            }
        } catch (error) {
            setFeedbackMessage('An error occurred. Please try again later.');
        }
    };

    const handleRegister = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch('http://localhost:8080/api/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ username, password, email }),
            });

            if (response.ok) {
                setFeedbackMessage('Registration successful! You can now log in.');
                setIsRegistering(false); // Switch back to login mode
            } else {
                setFeedbackMessage('Registration failed. Please try again.');
            }
        } catch (error) {
            setFeedbackMessage('An error occurred. Please try again later.');
        }
    };

    return (
        <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', flexDirection: 'column' }}>
            <h1>{isRegistering ? 'Register' : 'Login'}</h1>

            {feedbackMessage && <p>{feedbackMessage}</p>}

            <form onSubmit={isRegistering ? handleRegister : handleLogin} style={{ display: 'flex', flexDirection: 'column', width: '300px' }}>
                <label>
                    Username:
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </label>

                <label>
                    Password:
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </label>


                <button type="submit">{isRegistering ? 'Register' : 'Login'}</button>
            </form>

            <button
                onClick={() => setIsRegistering(!isRegistering)}
                style={{ marginTop: '10px', background: 'none', border: 'none', color: 'blue', cursor: 'pointer' }}
            >
                {isRegistering ? 'Already have an account? Login here' : "Don't have an account? Register here"}
            </button>
        </div>
    );
};

export default LoginPage;
