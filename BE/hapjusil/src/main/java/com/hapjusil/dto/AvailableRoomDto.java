package com.hapjusil.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AvailableRoomDto {
    private String practiceRoomId;
    private String roomId;
    private String practiceRoomName;
    private String address;
    private Integer price;
    private String imageUrl;
    private String roomName;  // RoomData의 name
}
