package com.hapjusil.domain;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "pr_hasbooking")
public class PrHasBooking {

    @Id
    @Column(name = "id")
    private String id;

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
    private String bookingBusinessId;

    @Column(name = "visitorReviewCount")
    private String visitorReviewCount;

    @Column(name = "visitorReviewScore")
    private String visitorReviewScore;

    @Column(name = "blogCafeReviewCount")
    private String blogCafeReviewCount;
}
