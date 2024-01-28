package com.hapjusil.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "practice_rooms")
@Getter
@Setter
public class PracticeRooms { // 크롤링한 서울 전체 합주실

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
//    private String id;
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "roadAddress")
    private String roadAddress;

    @Column(name = "address")
    private String address;

    @Column(name = "fullAddress")
    private String fullAddress;

    @Column(name = "commonAddress")
    private String commonAddress;

    @Column(name = "bookingUrl")
    private String bookingUrl;

    @Column(name = "phone")
    private String phone;

    @Column(name = "virtualPhone")
    private String virtualPhone;

    @Column(name = "imageUrl")
    private String imageUrl;

    @Column(name = "imageCount")
    private Integer imageCount;

    @Column(name = "x")
    private String x;

    @Column(name = "y")
    private String y;

    @Column(name = "hasBooking")
    private String hasBooking;

    @Column(name = "bookingBusinessId")
//    private String bookingBusinessId;
    private Long bookingBusinessId;

    @Column(name = "visitorReviewCount")
    private String visitorReviewCount;

    @Column(name = "visitorReviewScore")
    private String visitorReviewScore = "0";

    @Column(name = "blogCafeReviewCount")
    private String blogCafeReviewCount;

}