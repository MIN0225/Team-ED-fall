import React, { useState, useEffect } from "react";
import ListCard from "../components/Card/ListCard";
import Header from "../components/Header";
import Footer from "../components/Footer";
import RatingCard from "../components/Card/RatingCard";
import "../styles/pages/Rating.css";
import { MapContainer, TileLayer, Marker, Popup } from "react-leaflet";
import L from "leaflet";
import "leaflet/dist/leaflet.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faThumbsUp } from "@fortawesome/free-solid-svg-icons";
import markerImg from "../assets/marker.png";

function Rating() {
  const [cardData, setCardData] = useState([]);

  useEffect(() => {
    async function fetchCardData() {
      try {
        const response = await fetch("../practiceRooms.json");
        if (!response.ok) {
          throw new Error("Failed to fetch data");
        }
        const data = await response.json();
        setCardData(data);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    }

    fetchCardData();
  }, []);

  // 평점이 높은 순으로 정렬된 카드 데이터 (상위 4개만)
  const sortedCardData = [...cardData]
    .sort(
      (a, b) =>
        parseFloat(b.visitorReviewScore) - parseFloat(a.visitorReviewScore)
    )
    .slice(0, 4);

  const renderCards = () => {
    return sortedCardData.map((card, index) => (
      <RatingCard
        key={index}
        title={card.name}
        cost={card.cost}
        locate={card.fullAddress}
        content={<img src={card.imageUrl} alt="사진이 없습니다." />}
        rating={card.visitorReviewScore}
      />
    ));
  };

  const renderMapMarkers = () => {
    return cardData.map((card, index) => (
      <Marker
        key={index}
        position={[parseFloat(card.y), parseFloat(card.x)]}
        icon={L.icon({
          iconUrl: markerImg,
          iconSize: [25, 30],
        })}
      >
        <Popup>{card.name}</Popup>
      </Marker>
    ));
  };

  return (
    <div>
      <Header />

      <MapContainer
        center={[37.5562, 126.9239]}
        zoom={16}
        style={{ height: "400px", paddingTop: "10px", zIndex: "1" }}
      >
        <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
        {renderMapMarkers()}
      </MapContainer>
      <div>
        <div className="rating_title">
          평점 높은 순
          <span className="icon-container">
            <FontAwesomeIcon
              icon={faThumbsUp}
              className="far fa-thumbs-up thumbs-up-icon"
            />
          </span>
        </div>
        <div className="rating_card_pack">{renderCards()}</div>
      </div>

      <Footer />
    </div>
  );
}

export default Rating;
