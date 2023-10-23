import React from "react";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import Home from "./pages/Home.js";
import About from "./pages/About.js";
import Enroll from "./pages/Enroll.js";
import Rating from "./pages/Rating.js";
import Login from "./pages/Login.js";
import Heart from "./pages/Heart.js";

function App() {
  return (
    <Router>
      <div>
        <nav>
          <Link to="/"></Link>
        </nav>

        <Routes>
          {" "}
          <Route path="/" element={<Home />} />
          <Route path="/about" element={<About />} />
          <Route path="/rating" element={<Rating />} />
          <Route path="/enroll" element={<Enroll />} />
          <Route path="/login" element={<Login />} />
          <Route path="/heart" element={<Heart />} />
          {" "}
        </Routes>
      </div>
    </Router>
  );
}

export default App;
