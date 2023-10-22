package com.hapjusil.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PracticeRoomResponseDTO {
    private long id;
    private String name;
    private List<RoomDTO> rooms;

    @Getter
    @Setter
    public static class RoomDTO {
        private long id;
        private String roomName;
        private int price;
        private int maxCapacity;
    }
}
