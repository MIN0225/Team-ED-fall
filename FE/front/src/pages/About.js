import React, { useState, useEffect } from "react";
import ListCard from "../components/Card/ListCard";
import Header from "../components/Header";
import Footer from "../components/Footer";
import Filter from "../components/Dropdown/Filter";
import "../styles/pages/About.css";
import Pagination from "@mui/material/Pagination";

function About() {
  const [cardData, setCardData] = useState([]);
  const [selectedFilter, setSelectedFilter] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 8;
  const [filteredCardData, setFilteredCardData] = useState([]);
  const [uniqueCommonAddresses, setUniqueCommonAddresses] = useState([]);

  useEffect(() => {
    async function fetchCardData() {
      try {
        const response = await fetch("http://43.200.181.187:8080/practice-rooms/sorted-by-name?page=0&size=8");
        if (!response.ok) {
          throw new Error("Failed to fetch data");
        }
        const data = await response.json();
        
        const uniqueAddresses = [
          ...new Set(data.map((card) => card.commonAddress.trim())),
        ];
        uniqueAddresses.sort((a, b) => a.localeCompare(b));

        setUniqueCommonAddresses(uniqueAddresses);
        setCardData(data);
        setFilteredCardData(data);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    }

    fetchCardData();
  }, []);

  const renderFilteredCards = () => {
    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;

    return filteredCardData
      .slice(startIndex, endIndex)
      .map((card, index) => (
        <ListCard
          key={index}
          title={card.name}
          cost={card.cost}
          locate={card.fullAddress}
          content={<img src={card.imageUrl} alt="사진이 없습니다." />}
        />
      ));
  };

  const handleFilterChange = (selectedValue) => {
    setSelectedFilter(selectedValue);

    const filteredData = cardData.filter(
      (card) => card.commonAddress === selectedValue
    );

    setFilteredCardData(filteredData);
    setCurrentPage(1);
  };

  const handlePageChange = (event, newPage) => {
    setCurrentPage(newPage);
  };

  const totalPages = Math.ceil(filteredCardData.length / itemsPerPage);

  return (
    <div>
      <Header />
      <div className="filter-container">
        <Filter
          options={uniqueCommonAddresses}
          onChange={handleFilterChange}
        />
      </div>
      <div className="card_pack init_height">{renderFilteredCards()}</div>
      <div className="pagination">
        <Pagination
          count={totalPages}
          page={currentPage}
          onChange={handlePageChange}
        />
      </div>
      <Footer />
    </div>
  );
}

export default About;
