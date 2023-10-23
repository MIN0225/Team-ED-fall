import React from "react";
import ListCard from "../components/Card/ListCard";
import Header from "../components/Header";
import Footer from "../components/Footer";
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

  const renderCards = () => {
    return cardData.map((card, index) => (
      <ListCard
        key={index}
        title={card.title}
        cost={card.cost}
        locate={card.locate}
        content={card.content}
      />
    ));
  };

  return (
    <div>
      <Header />
      <div>필터 추가 예정</div>
      <div className="card_pack">{renderCards()}</div>
      <Footer />
    </div>
  );
}

export default About;
