package com.hapjusil.dto.PracticeRooms;

import com.hapjusil.dto.RoomInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PracticeRoomsWithRoomDataResponseDTO {
    private Long practiceRoomId;
    private String practiceRoomName; // 합주실 이름
    private String roadAddress;
    private String bookingUrl; // 있을 수도, 없을 수도
    private String imageUrl;
//    private String phone;
//    private String visitorReviewScore; // 추가된 필드
    private String phoneNumber; // phone이랑 visitorReviewScore중 있는거
    private String fullAddress; // 주소
    private List<RoomInfo> roomInfoList; // 연습실 아이디, 이름, 가격

}