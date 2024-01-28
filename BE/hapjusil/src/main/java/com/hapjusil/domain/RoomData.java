package com.hapjusil.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "room_datas")
@Getter
@Setter
public class RoomData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
//    private String roomId;
    private Long roomId;

    @Column(name = "pr_id")
    private Long prId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price")
    private Integer price;

}
