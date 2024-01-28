package com.hapjusil.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomInfo { // 합주실에 포함된 연습실 정보
    private Long roomId; // 연스실 아이디
    private String roomName; // 연습실 이름
    private Integer price; // 연습실 시간당 가격
}
