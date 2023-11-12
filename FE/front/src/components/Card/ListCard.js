import React, { useState } from "react";
import "../../styles/components/Card/ListCard.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faHeart, faMapMarkerAlt } from "@fortawesome/free-solid-svg-icons";

function ListCard({ title, content, cost, locate }) {
  const [isFavorite, setIsFavorite] = useState(false);

  const toggleFavorite = () => {
    setIsFavorite(!isFavorite);
  };

  return (
    <div className="container">
      <div className="title_box">
        <div className="card1">
          <div className="card_title">{title}</div>
          <div className="card_cost">{cost}</div>
        </div>
        <div className="card2">
          <div className="card_locate">
            <FontAwesomeIcon icon={faMapMarkerAlt} />
            {locate}
          </div>
          <div
            className={`card_heart ${isFavorite ? "active" : ""}`}
            onClick={toggleFavorite}
          >
            <FontAwesomeIcon icon={faHeart} />
          </div>
        </div>
      </div>
      <div className="content_box">
        <div>{content}</div>
      </div>
    </div>
  );
}

export default ListCard;
