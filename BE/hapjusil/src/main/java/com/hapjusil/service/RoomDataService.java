package com.hapjusil.service;

import com.hapjusil.domain.PracticeRooms;
import com.hapjusil.domain.RoomData;
import com.hapjusil.dto.RoomDataRequestDTO;
import com.hapjusil.repository.PracticeRoomsRepository;
import com.hapjusil.repository.RoomDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RoomDataService {

    @Autowired
    PracticeRoomsRepository practiceRoomsRepository;

    @Autowired
    RoomDataRepository roomDataRepository;

    public RoomData createRoomData(Long practiceRoomsId, RoomDataRequestDTO roomDataRequestDTO) {

        PracticeRooms practiceRooms = practiceRoomsRepository.findById(practiceRoomsId).orElseThrow(() -> new IllegalArgumentException("해당 합주실이 없습니다. id=" + practiceRoomsId));
        RoomData roomData = new RoomData();

        if(practiceRooms.getBookingBusinessId() != null) {
            roomData.setName(roomDataRequestDTO.getRoomName());
            roomData.setPrice(roomDataRequestDTO.getPrice());
            roomData.setPrId(practiceRooms.getId());
        } else {
            roomData.setName(roomDataRequestDTO.getRoomName());
            roomData.setPrice(roomDataRequestDTO.getPrice());
        }

        roomDataRepository.save(roomData);

        return null;
    }

    public ResponseEntity<RoomData> updateRoomData(Long roomDataId, RoomDataRequestDTO roomDataRequestDTO) {
        RoomData roomData = roomDataRepository.findById(roomDataId).orElseThrow(() -> new IllegalArgumentException("해당 합주실이 없습니다. id=" + roomDataId));

        roomData.setName(roomDataRequestDTO.getRoomName());
        roomData.setPrice(roomDataRequestDTO.getPrice());

        roomDataRepository.save(roomData);

        return ResponseEntity.ok(roomData);
    }

}