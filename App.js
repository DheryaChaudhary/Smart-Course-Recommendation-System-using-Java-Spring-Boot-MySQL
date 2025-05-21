import React, { useState, useEffect } from "react";
import axios from "axios";

const API = "http://localhost:8080/api";

function App() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [token, setToken] = useState(localStorage.getItem("token") || "");
  const [skills, setSkills] = useState("");
  const [recommendations, setRecommendations] = useState([]);
  const [favorites, setFavorites] = useState([]);

  const login = async () => {
    try {
      const res = await axios.post(`${API}/auth/login`, { email, password });
      setToken(res.data.token);
      localStorage.setItem("token", res.data.token);
      alert("Login successful!");
    } catch {
      alert("Login failed!");
    }
  };

  const register = async () => {
    try {
      await axios.post(`${API}/auth/register`, { email, password, name: email });
      alert("Registered! Please login.");
    } catch {
      alert("Register failed!");
    }
  };

  const updateSkills = async () => {
    try {
      await axios.post(
        `${API}/user/skills`,
        { skills: skills.split(",").map(s => s.trim().toLowerCase()) },
        { headers: { Authorization: `Bearer ${token}` } }
      );
      alert("Skills updated!");
    } catch {
      alert("Failed to update skills");
    }
  };

  const fetchRecommendations = async () => {
    try {
      const res = await axios.get(`${API}/user/recommendations`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setRecommendations(res.data);
    } catch {
      alert("Failed to fetch recommendations");
    }
  };

  const addFavorite = async (id) => {
    try {
      await axios.post(`${API}/user/favorites/${id}`, {}, {
        headers: { Authorization: `Bearer ${token}` },
      });
      alert("Added to favorites");
      fetchFavorites();
    } catch {
      alert("Failed to add favorite");
    }
  };

  const fetchFavorites = async () => {
    try {
      const res = await axios.get(`${API}/user/favorites`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setFavorites(res.data);
    } catch {
      alert("Failed to fetch favorites");
    }
  };

  useEffect(() => {
    if (token) {
      fetchRecommendations();
      fetchFavorites();
    }
  }, [token]);

  if (!token)
    return (
      <div style={{ padding: 20 }}>
        <h2>Login or Register</h2>
        <input placeholder="Email" value={email} onChange={e => setEmail(e.target.value)} />
        <br />
        <input type="password" placeholder="Password" value={password} onChange={e => setPassword(e.target.value)} />
        <br />
        <button onClick={login}>Login</button>
        <button onClick={register}>Register</button>
      </div>
    );

  return (
    <div style={{ padding: 20 }}>
      <h2>Smart Course Recommender</h2>

      <div>
        <h4>Update Skills (comma separated)</h4>
        <input
          placeholder="e.g. java, react, python"
          value={skills}
          onChange={e => setSkills(e.target.value)}
          style={{ width: "300px" }}
        />
        <button onClick={updateSkills}>Update</button>
      </div>

      <div>
        <h4>Recommended Courses</h4>
        <button onClick={fetchRecommendations}>Refresh Recommendations</button>
        <ul>
          {recommendations.map(course => (
            <li key={course.id}>
              <a href={course.url} target="_blank" rel="noopener noreferrer">{course.title}</a> by {course.provider}
              <button onClick={() => addFavorite(course.id)}>Add to Favorites</button>
            </li>
          ))}
        </ul>
      </div>

      <div>
        <h4>Favorite Courses</h4>
        <ul>
          {favorites.map(course => (
            <li key={course.id}>
              <a href={course.url} target="_blank" rel="noopener noreferrer">{course.title}</a> by {course.provider}
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
}

export default App;
