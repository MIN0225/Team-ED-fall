import React from "react";
import ListCard from "../components/Card/ListCard";
import Header from "../components/Header";
import Footer from "../components/Footer";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faHeart } from "@fortawesome/free-solid-svg-icons";
import "../styles/pages/Heart.css";

function Heart() {
  const cardData = [
    {
      title: "합주실1",
      cost: "가격",
      locate: "위치",
      content: "사진 추가 예정",
    },
    {
      title: "합주실2",
      cost: "가격",
      locate: "위치",
      content: "사진 추가 예정",
    },
    {
      title: "합주실3",
      cost: "가격",
      locate: "위치",
      content: "사진 추가 예정",
    },
    {
      title: "합주실4",
      cost: "가격",
      locate: "위치",
      content: "사진 추가 예정",
    },
    {
      title: "합주실5",
      cost: "가격",
      locate: "위치",
      content: "사진 추가 예정",
    },
    {
      title: "합주실6",
      cost: "가격",
      locate: "위치",
      content: "사진 추가 예정",
    },
    {
      title: "합주실7",
      cost: "가격",
      locate: "위치",
      content: "사진 추가 예정",
    },
    {
      title: "합주실8",
      cost: "가격",
      locate: "위치",
      content: "사진 추가 예정",
    },
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
      <div className="heart_title">
        찜한 합주실
        <span className="icon-container">
          <FontAwesomeIcon
            icon={faHeart}
            className="far fa-heart heart-icon"
          />
        </span>
      </div>
      <div className="card_pack">{renderCards()}</div>
      <Footer />
    </div>
  );
}

export default Heart;
