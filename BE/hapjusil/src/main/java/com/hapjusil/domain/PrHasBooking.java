package com.hapjusil.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "pr_hasbooking")
public class PrHasBooking {
    @Id
    private String id;
    private String name;
    private String roadAddress;
    private String address;
    private String fullAddress;
    private String commonAddress;
    private String bookingUrl;
    private String phone;
    private String virtualPhone;
    private String imageUrl;
    private Integer imageCount;
    private String x;
    private String y;
    private String hasBooking;
    private String bookingBusinessId;
    private String visitorReviewCount;
    private String visitorReviewScore;
    private String blogCafeReviewCount;

}