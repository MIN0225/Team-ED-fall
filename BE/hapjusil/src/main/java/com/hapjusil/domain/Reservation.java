package com.hapjusil.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "pr_id")
    private Long prId; // practice_rooms 테이블의 id 컬럼

    @JoinColumn(name = "room_data_id")
    private Long roomDataId; // room_datas 테이블의 id 컬럼

    private LocalDate reservationDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Column(name = "status", nullable = false, columnDefinition = "ENUM('PENDING', 'CONFIRMED', 'CANCELLED', 'COMPLETED', 'NO_SHOW') DEFAULT 'PENDING'")
    @Enumerated(EnumType.STRING)
    private ReservationStatus status = ReservationStatus.PENDING;

//    @ManyToOne
//    @JoinColumn(name = "roomID")
//    private Room room;

    @ManyToOne
    private RoomData roomData;
}

