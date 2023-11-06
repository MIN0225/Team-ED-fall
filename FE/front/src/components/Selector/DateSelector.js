import React from "react";
import DatePicker from "react-datepicker";
import { ko } from "date-fns/esm/locale";
import "react-datepicker/dist/react-datepicker.css";
import styled from "styled-components";

function DateSelector({ selectedDate, setSelectedDate }) {
  const handleDateChange = (date) => {
    setSelectedDate(date);
  };

  return (
    <CalendarContainer>
      <DatePicker
        locale={ko}
        selected={selectedDate}
        onChange={handleDateChange}
        dateFormat="yyyy/MM/dd"
        isClearable
        inline
      />
    </CalendarContainer>
  );
}

export default DateSelector;

const CalendarContainer = styled.div`
  .react-datepicker {
    width: 300px;
  }
  .react-datepicker__month-container {
    float: none;
  }
  .react-datepicker__header {
    border-bottom: none;
    background-color: white;
  }
  .react-datepicker__navigation-icon::before {
    border-color: black;
    border-width: 2px 2px 0 0;
  }
  .react-datepicker__day--selected {
    border-radius: 20px;
    background-color: rgba(56, 85, 106, 0.4);
    color: black;
  }
  .react-datepicker__day--keyboard-selected {
    border-radius: 20px;
    background-color: rgba(182, 255, 249,0.6);
    color: black;
  }
  .react-datepicker__day-names {
    color: rgba(39, 57, 82);
    padding-top: 10px;
    margin-bottom: -12px;
    font-size: smaller;
  }
  .react-datepicker__current-month,
  .react-datepicker-time__header,
  .react-datepicker-year-header {
    margin-top: 0;
    color: rgba(39, 57, 82);
    font-weight: 18;
    font-size: 16px;
  }
`;
