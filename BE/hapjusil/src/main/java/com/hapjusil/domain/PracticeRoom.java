package com.hapjusil.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "practice_room")
public class PracticeRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // 합주실ID

    private String name; // 합주실이름

    private String thumbnail; // 썸네일 이미지 링크

    private String phoneNumber; // 합주실 전화번호

    private String website; // 합주실 웹사이트 링크

    private String location; // 합주실 위치

    @Column(nullable = false)
    private LocalDate registrationDate = LocalDate.now(); // 합주실 등록일

    private double rate; // 합주실 평점

    private double latitude; // 위도

    private double longitude; // 경도

    @OneToMany(mappedBy = "practiceRoom", fetch = FetchType.EAGER)
    private List<Room> rooms;
}
