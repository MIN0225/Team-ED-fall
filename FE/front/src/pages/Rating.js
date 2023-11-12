import React from "react";
import ListCard from "../components/Card/ListCard";
import Header from "../components/Header";
import Footer from "../components/Footer";
import "../styles/pages/Rating.css";
import { MapContainer, TileLayer, Marker, Popup } from "react-leaflet";
import L from "leaflet";
import "leaflet/dist/leaflet.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faThumbsUp } from "@fortawesome/free-solid-svg-icons";
import markerImg from "../assets/marker.png";

function Rating() {
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

      <MapContainer
        center={[37.5599, 127.0027]}
        zoom={16}
        style={{ height: "400px", paddingTop: "10px", zIndex: "1" }}
      >
        <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
        <Marker
          position={[37.5599, 127.0027]}
          icon={L.icon({
            iconUrl: markerImg,
            iconSize: [25, 30], 
          })}
        >
          <Popup>합주실 A</Popup>
        </Marker>
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
