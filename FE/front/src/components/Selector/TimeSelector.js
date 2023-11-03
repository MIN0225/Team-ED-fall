import React, { useState } from "react";
import "../../styles/components/Selector/TimeSelector.css";

function TimeSelector({ selectedTimes, setSelectedTimes }) {
  const handleTimeClick = (time) => {
    if (selectedTimes.includes(time)) {
      setSelectedTimes(selectedTimes.filter((selectedTime) => selectedTime !== time));
    } else {
      setSelectedTimes([...selectedTimes, time]);
    }
  };

  const renderTimeButtons = (timeArray) => {
    return timeArray.map((time) => (
      <button
        key={time}
        className={`time_btn ${selectedTimes.includes(time) ? "selected" : ""}`}
        onClick={() => handleTimeClick(time)}
      >
        <div className="time_btn_text">{time}</div>
      </button>
    ));
  };

  return (
    <div className="time_box">
      <div className="AM">
        <p className="time_text">오전</p>
        {renderTimeButtons([
          "8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM"
        ])}
      </div>
      <p></p>
      <div className="PM">
        <p className="time_text">오후</p>
        {renderTimeButtons([
          "12:00 PM", "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM",
          "6:00 PM", "7:00 PM", "8:00 PM", "9:00 PM", "10:00 PM", "11:00 PM"
        ])}
      </div>
    </div>
  );
}

export default TimeSelector;
