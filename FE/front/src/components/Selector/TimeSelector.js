import React from "react";
import "../../styles/components/Selector/TimeSelector.css";

function TimeSelector({ selectedTime, setSelectedTime }) {
  const handleTimeClick = (time) => {
    setSelectedTime(time);
  };

  return (
    <div className="time_box">
      <div>
        <p className="time_text">오전</p>
        <button
          className={`time_btn ${selectedTime === "8:00 AM" ? "selected" : ""}`}
          onClick={() => handleTimeClick("8:00 AM")}
        >
          <div className="time_btn_text">8:00</div>
        </button>
        <button
          className={`time_btn ${selectedTime === "9:00 AM" ? "selected" : ""}`}
          onClick={() => handleTimeClick("9:00 AM")}
        >
          <div className="time_btn_text">9:00</div>
        </button>
        <button
          className={`time_btn ${
            selectedTime === "10:00 AM" ? "selected" : ""
          }`}
          onClick={() => handleTimeClick("10:00 AM")}
        >
          <div className="time_btn_text">10:00</div>
        </button>
        <button
          className={`time_btn ${
            selectedTime === "11:00 AM" ? "selected" : ""
          }`}
          onClick={() => handleTimeClick("11:00 AM")}
        >
          <div className="time_btn_text">11:00</div>
        </button>
      </div>
      <p></p>
      <div>
        <p className="time_text">오후</p>
        <button
          className={`time_btn ${
            selectedTime === "12:00 PM" ? "selected" : ""
          }`}
          onClick={() => handleTimeClick("12:00 PM")}
        >
          <div className="time_btn_text">12:00</div>
        </button>
        <button
          className={`time_btn ${selectedTime === "1:00 PM" ? "selected" : ""}`}
          onClick={() => handleTimeClick("1:00 PM")}
        >
          <div className="time_btn_text">1:00</div>
        </button>
        <button
          className={`time_btn ${selectedTime === "2:00 PM" ? "selected" : ""}`}
          onClick={() => handleTimeClick("2:00 PM")}
        >
          <div className="time_btn_text">2:00</div>
        </button>
        <button
          className={`time_btn ${selectedTime === "3:00 PM" ? "selected" : ""}`}
          onClick={() => handleTimeClick("3:00 PM")}
        >
          <div className="time_btn_text">3:00</div>
        </button>
        <button
          className={`time_btn ${selectedTime === "4:00 PM" ? "selected" : ""}`}
          onClick={() => handleTimeClick("4:00 PM")}
        >
          <div className="time_btn_text">4:00</div>
        </button>
        <button
          className={`time_btn ${selectedTime === "5:00 PM" ? "selected" : ""}`}
          onClick={() => handleTimeClick("5:00 PM")}
        >
          <div className="time_btn_text">5:00</div>
        </button>
        <button
          className={`time_btn ${selectedTime === "6:00 PM" ? "selected" : ""}`}
          onClick={() => handleTimeClick("6:00 PM")}
        >
          <div className="time_btn_text">6:00</div>
        </button>
        <button
          className={`time_btn ${selectedTime === "7:00 PM" ? "selected" : ""}`}
          onClick={() => handleTimeClick("7:00 PM")}
        >
          <div className="time_btn_text">7:00</div>
        </button>
        <button
          className={`time_btn ${selectedTime === "8:00 PM" ? "selected" : ""}`}
          onClick={() => handleTimeClick("8:00 PM")}
        >
          <div className="time_btn_text">8:00</div>
        </button>
        <button
          className={`time_btn ${selectedTime === "9:00 PM" ? "selected" : ""}`}
          onClick={() => handleTimeClick("9:00 PM")}
        >
          <div className="time_btn_text">9:00</div>
        </button>
        <button
          className={`time_btn ${
            selectedTime === "10:00 PM" ? "selected" : ""
          }`}
          onClick={() => handleTimeClick("10:00 PM")}
        >
          <div className="time_btn_text">10:00</div>
        </button>
        <button
          className={`time_btn ${
            selectedTime === "11:00 PM" ? "selected" : ""
          }`}
          onClick={() => handleTimeClick("11:00 PM")}
        >
          <div className="time_btn_text">11:00</div>
        </button>
      </div>
    </div>
  );
}

export default TimeSelector;
