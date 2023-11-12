import React, { useState } from "react";
import ListCard from "../components/Card/ListCard";
import Header from "../components/Header";
import Footer from "../components/Footer";
import Filter from "../components/Dropdown/Filter"; // 필터 드롭다운 컴포넌트 추가
import "../styles/pages/About.css";

function About() {
  const cardData = [
    { title: "합주실1", cost: "가격", locate: "위치", content: "사진 추가 예정" },
    { title: "합주실2", cost: "가격", locate: "위치", content: "사진 추가 예정" },
    { title: "합주실3", cost: "가격", locate: "위치", content: "사진 추가 예정" },
    { title: "합주실4", cost: "가격", locate: "위치", content: "사진 추가 예정" },
    { title: "합주실5", cost: "가격", locate: "위치", content: "사진 추가 예정" },
    { title: "합주실6", cost: "가격", locate: "위치", content: "사진 추가 예정" },
    { title: "합주실7", cost: "가격", locate: "위치", content: "사진 추가 예정" },
    { title: "합주실8", cost: "가격", locate: "위치", content: "사진 추가 예정" },
  ];

  const [selectedFilter, setSelectedFilter] = useState(""); // 선택한 필터

  const renderFilteredCards = () => {
    if (selectedFilter === "") {
      return cardData.map((card, index) => (
        <ListCard
          key={index}
          title={card.title}
          cost={card.cost}
          locate={card.locate}
          content={card.content}
        />
      ));
    } else {
      return cardData
        .filter((card) => card.title.includes(selectedFilter))
        .map((filteredCard, index) => (
          <ListCard
            key={index}
            title={filteredCard.title}
            cost={filteredCard.cost}
            locate={filteredCard.locate}
            content={filteredCard.content}
          />
        ));
    }
  };

  const handleFilterChange = (selectedValue) => {
    setSelectedFilter(selectedValue);
  };

  return (
    <div>
      <Header />
      <div className="filter-container">
        <Filter options={["합주실1", "합주실2", "합주실3"]} onChange={handleFilterChange} />
      </div>
      <div className="card_pack init_height">{renderFilteredCards()}</div>
      <Footer />
    </div>
  );
}

export default About;
