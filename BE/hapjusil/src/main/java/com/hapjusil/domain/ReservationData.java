package com.hapjusil.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "reservation_datas")
@IdClass(ReservationData.ReservationDataId.class)
@Getter
@Setter
public class ReservationData {

    @Id
    @Column(name = "room_id")
    private Integer roomId;

    @Id
    @Column(name = "available_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date availableTime;

    // getters and setters

    public static class ReservationDataId implements Serializable {
        private Integer roomId;
        private Date availableTime;

    }
}