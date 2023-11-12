package com.hapjusil.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class UserFavorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "practice_roomid")
    private PracticeRoom practiceRoom;

    @ManyToOne
    @JoinColumn(name = "PracticeRoomsID")
    private PracticeRooms practiceRooms;
}