package com.hapjusil.domain;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
public class Room {

    @Id
    private long id; // 연습실ID

    private String roomName;
    private int price;
    private int maxCapacity;

    @ManyToOne
    @JoinColumn(name = "practiceRoomID")
    private PracticeRoom practiceRoom;

//    @OneToMany(mappedBy = "room")
//    private List<Reservation> reservations;
}
