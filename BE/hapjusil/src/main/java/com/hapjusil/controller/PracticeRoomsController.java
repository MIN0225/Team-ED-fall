package com.hapjusil.controller;

import com.hapjusil.domain.PracticeRooms;
import com.hapjusil.dto.PracticeRooms.PracticeRoomsRequestDTO;
import com.hapjusil.dto.PracticeRooms.PracticeRoomsWithRoomDataResponseDTO;
import com.hapjusil.service.PracticeRoomsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pr")
@CrossOrigin("*")
public class PracticeRoomsController {

    @Autowired
    private PracticeRoomsService practiceRoomsService;

    @PostMapping("/add")
    public ResponseEntity<PracticeRooms> addPracticeRoom(@RequestBody PracticeRoomsRequestDTO practiceRoomsRequestDTO) {
        return practiceRoomsService.addPracticeRoom(practiceRoomsRequestDTO);
    }

    @GetMapping("/search-by-address")
    public ResponseEntity<List<PracticeRooms>> searchPracticeRoomByAddress(@RequestParam String fullAddress) {
        return practiceRoomsService.searchPracticeRoomsByAddress(fullAddress);
    }

    @PutMapping("/{id}") // PracticeRooms 테이블의 id
    public ResponseEntity<PracticeRooms> updatePracticeRooms(
            @PathVariable("id") long id,
            @RequestBody PracticeRoomsRequestDTO practiceRoomsRequestDTO){
        return practiceRoomsService.updatePracticeRooms(id, practiceRoomsRequestDTO);
    }

    @PutMapping("{id}/roadAddress")
    public ResponseEntity<PracticeRooms> updatePracticeRoomsRoadAddress(
        @PathVariable("id") long id,
        @RequestParam String roadAddress){
        return practiceRoomsService.updatePracticeRoomsRoadAddress(id, roadAddress);
    }

    @PutMapping("{id}/fullAddress")
    public ResponseEntity<PracticeRooms> updatePracticeRoomsFullAddress(
        @PathVariable("id") long id,
        @RequestParam String fullAddress){
        return practiceRoomsService.updatePracticeRoomsFullAddress(id, fullAddress);
    }

    @PutMapping("{id}/phoneNumber")
    public ResponseEntity<PracticeRooms> updatePracticeRoomsPhone(
        @PathVariable("id") long id,
        @RequestParam String phoneNumber){
        return practiceRoomsService.updatePracticeRoomsPhone(id, phoneNumber);
    }

    @PutMapping("{id}/bookingUrl")
    public ResponseEntity<PracticeRooms> updatePracticeRoomsBookingUrl(
        @PathVariable("id") long id,
        @RequestParam String bookingUrl){
        return practiceRoomsService.updatePracticeRoomsBookingUrl(id, bookingUrl);
    }

    @GetMapping("/search-by-id/{practiceRoomsId}")
    public PracticeRoomsWithRoomDataResponseDTO searchPracticeRoomById(@PathVariable Long practiceRoomsId) { // practice_rooms 테이블의 bookingBusinessId
        return practiceRoomsService.searchPracticeRoomById(practiceRoomsId);
    }
}