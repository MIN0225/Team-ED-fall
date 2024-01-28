package com.hapjusil.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AvailableRoom2Dto {
    private Long practiceRoomId;
    private String practiceRoomName; // 합주실 이름
    private String address;
    private String imageUrl;
    private List<RoomInfo> roomInfoList; // 연습실 아이디, 이름, 가격

    @Override
    public String toString() {
        return "AvailableRoom2Dto{" +
                "practiceRoomId='" + practiceRoomId + '\'' +
                ", practiceRoomName='" + practiceRoomName + '\'' +
                ", address='" + address + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", roomInfoList=" + roomInfoList +
                '}';
    }
}
