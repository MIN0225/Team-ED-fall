import React, { useState } from "react";
import "../../styles/components/Card/MainCard.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faHeart, faMapMarkerAlt } from "@fortawesome/free-solid-svg-icons";

function MainCard({ card }) {
  const [isFavorite, setIsFavorite] = useState(card.isFavorite);

  const toggleFavorite = () => {
    setIsFavorite(!isFavorite);
  };

  return (
    <div className="main_container">
      <div className="main_title_box">
        <div className="main_card1">
          <div className="main_card_title">{card.title}</div>
          <div
            className={`main_card_heart ${isFavorite ? "active" : ""}`}
            onClick={toggleFavorite}
          >
            <FontAwesomeIcon icon={faHeart} />
          </div>
        </div>
        <div className="main_card2">
          <div className="main_card_locate">
            <FontAwesomeIcon icon={faMapMarkerAlt} />
            {card.locate}
          </div>
          <div className="main_card_cost">{card.cost}</div>
        </div>
      </div>
      <div className="main_content_box">
        <div>{card.content}</div>
      </div>
    </div>
  );
}

export default MainCard;
