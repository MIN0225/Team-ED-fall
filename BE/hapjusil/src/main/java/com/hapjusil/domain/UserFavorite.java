package com.hapjusil.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Optional;

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

//    @Column(name = "userid")
//    private Long userId;

//    @ManyToOne
//    @JoinColumn(name = "practice_roomid")
//    private PracticeRoom practiceRoom;

    @ManyToOne
    @JoinColumn(name = "PracticeRoomsID")
    private PracticeRooms practiceRooms;

//    @Column(name = "practice_roomid")
//    private Long practiceRoomsId;

    public Optional<Object> getSelectedPracticeRoom() {
        if (this.practiceRooms != null) {
            // PracticeRooms 타입을 반환하는 로직 (여기서는 단순화를 위해 this.practiceRooms를 반환)
            return Optional.of(this.practiceRooms);
//       { else if (this.practiceRoom != null) {
//            // PracticeRoom 타입을 반환하는 로직
//            return Optional.of(this.practiceRoom);
        } else {
            return Optional.empty();
        }
    }
}