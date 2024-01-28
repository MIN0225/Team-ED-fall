package com.hapjusil.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationRequestDto {
    private Long prId; // practice_rooms 테이블의 id 컬럼
    private Long roomDataId; // room_datas 테이블의 id 컬럼
    private LocalDate reservationDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

}
