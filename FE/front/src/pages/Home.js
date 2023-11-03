import React, { useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faArrowLeft, faArrowRight } from "@fortawesome/free-solid-svg-icons";
import MainCard from "../components/Card/MainCard";
import Header from "../components/Header";
import Footer from "../components/Footer";
import DateSelector from "../components/Selector/DateSelector";
import TimeSelector from "../components/Selector/TimeSelector";
import "../styles/pages/Home.css";

function Home() {
  const [selectedDate, setSelectedDate] = useState(null);
  const [selectedTimes, setSelectedTimes] = useState([]);

  const [cards, setCards] = useState([
    {
      id: 1,
      title: "합주실 1",
      cost: "가격 1",
      locate: "위치 1",
      content: "카드 내용 1",
      isFavorite: false,
    },
    {
      id: 2,
      title: "합주실 2",
      cost: "가격 2",
      locate: "위치 2",
      content: "카드 내용 2",
      isFavorite: true,
    },
    {
      id: 3,
      title: "합주실 3",
      cost: "가격 3",
      locate: "위치 3",
      content: "카드 내용 3",
      isFavorite: false,
    },
    {
      id: 4,
      title: "합주실 4",
      cost: "가격 4",
      locate: "위치 4",
      content: "카드 내용 4",
      isFavorite: false,
    },
    {
      id: 5,
      title: "합주실 5",
      cost: "가격 5",
      locate: "위치 5",
      content: "카드 내용 5",
      isFavorite: false,
    },
  ]);

  const [currentIndex, setCurrentIndex] = useState(0);

  const nextCard = () => {
    const nextIndex = (currentIndex + 2) % cards.length;
    setCurrentIndex(nextIndex);
  };

  const prevCard = () => {
    const prevIndex = (currentIndex - 2 + cards.length) % cards.length;
    setCurrentIndex(prevIndex);
  };

  const currentCard1 = cards[currentIndex];
  const currentCard2 = cards[(currentIndex + 1) % cards.length];

  const toggleFavorite = (id) => {
    const updatedCards = cards.map((card) =>
      card.id === id ? { ...card, isFavorite: !card.isFavorite } : card
    );
    setCards(updatedCards);
  };

  const selectedDateTime =
    selectedDate && selectedTimes.length > 0
      ? `${selectedDate.toLocaleDateString()} ${selectedTimes.join(", ")}`
      : null;

  return (
    <div>
      <Header />
      <div className={`content ${selectedDateTime ? "content-shifted" : ""}`}>
        <p className="time_title">원하는 시간을 선택하세요</p>
        <div className="selector">
          <DateSelector
            selectedDate={selectedDate}
            setSelectedDate={setSelectedDate}
          />
          <p />
          <TimeSelector selectedTimes={selectedTimes} setSelectedTimes={setSelectedTimes} />
          {selectedDateTime && (
            <p className="datetime">선택한 날짜와 시간: {selectedDateTime}</p>
          )}
        </div>
        {selectedDateTime && (
          <div className="maincard_box">
            <div className="nav_buttons">
              <button onClick={prevCard}>
                <FontAwesomeIcon icon={faArrowLeft} />
              </button>
            </div>
            <div className="maincard_group">
              <MainCard
                card={currentCard1}
                toggleFavorite={() => toggleFavorite(currentCard1.id)}
              />
              <MainCard
                card={currentCard2}
                toggleFavorite={() => toggleFavorite(currentCard2.id)}
              />
            </div>
            <div className="nav_buttons">
              <button onClick={nextCard}>
                <FontAwesomeIcon icon={faArrowRight} />
              </button>
            </div>
          </div>
        )}
      </div>
      <Footer />
    </div>
  );
}

export default Home;
