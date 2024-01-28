package com.hapjusil.dto.PracticeRooms;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PracticeRoomsRequestDTO { // 크롤링 합주실 테이블 DTO
    private String name;
    private String imageUrl;
    private String phone;
    private String bookingUrl;
    private String fullAddress;
    private String x;
    private String y;
}
