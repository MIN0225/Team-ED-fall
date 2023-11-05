import React, { useState, useEffect, useRef } from "react";
import { FaBars, FaSearch } from "react-icons/fa";
import Sidebar from "./Sidebar";
import "../styles/components/Header.css";

function Header() {
  const [searchQuery, setSearchQuery] = useState("");
  const [sidebarOpen, setSidebarOpen] = useState(false);

  const handleSearchChange = (e) => {
    setSearchQuery(e.target.value);
  };

  const toggleSidebar = () => {
    setSidebarOpen(!sidebarOpen);
  };

  const handleClickOutside = (e) => {
    if (sidebarRef.current && !sidebarRef.current.contains(e.target)) {
      setSidebarOpen(false);
    }
  };

  useEffect(() => {
    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

  const sidebarRef = useRef(null);

  const handleNameClick = () => {
    window.location.href = "/";
  };

  return (
    <div>
      <header className="title">
        <div className="menu-icon" onClick={toggleSidebar}>
          <FaBars className="icon" />
        </div>
        <h2 className="name" onClick={handleNameClick}>
          합주실 통합 검색 시스템
        </h2>
        <div className="search">
          <input
            className="inputSearch"
            type="text"
            placeholder="검색어를 입력하세요"
            value={searchQuery}
            onChange={handleSearchChange}
          />
          <button onClick={() => alert(`검색어: ${searchQuery}`)}>
            <FaSearch />
          </button>
        </div>
      </header>
      <Sidebar
        isOpen={sidebarOpen}
        toggleSidebar={toggleSidebar}
        sidebarRef={sidebarRef}
      />
    </div>
  );
}

export default Header;
