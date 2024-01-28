package com.hapjusil.controller;

import com.hapjusil.domain.RoomData;
import com.hapjusil.dto.RoomDataRequestDTO;
import com.hapjusil.service.RoomDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/room-data")
@CrossOrigin("*")
public class RoomDataController {

    @Autowired
    RoomDataService roomDataService;

    @PostMapping("/{practiceRoomsId}")
    public RoomData createRoomData(@PathVariable Long practiceRoomsId,
    @RequestBody RoomDataRequestDTO roomDataRequestDTO) {
        return roomDataService.createRoomData(practiceRoomsId, roomDataRequestDTO);
    }

    @PutMapping("/{roomDataId}")
    public ResponseEntity<RoomData> updateRoomData(@PathVariable Long roomDataId,
        @RequestBody RoomDataRequestDTO roomDataRequestDTO) {
        return roomDataService.updateRoomData(roomDataId, roomDataRequestDTO);
    }

}