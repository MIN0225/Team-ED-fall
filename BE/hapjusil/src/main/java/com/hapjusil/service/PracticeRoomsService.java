package com.hapjusil.service;

import com.hapjusil.domain.PracticeRooms;
import com.hapjusil.domain.RoomData;
import com.hapjusil.dto.PracticeRooms.PracticeRoomsRequestDTO;
import com.hapjusil.dto.PracticeRooms.PracticeRoomsWithRoomDataResponseDTO;
import com.hapjusil.dto.RoomInfo;
import com.hapjusil.repository.PracticeRoomsRepository;
import com.hapjusil.repository.RoomDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PracticeRoomsService { // 크롤링 합주실 테이블 서비스
    @Autowired
    private PracticeRoomsRepository practiceRoomsRepository;


    @Autowired
    private RoomDataRepository roomDataRepository;

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PracticeRoomsService.class);

    public ResponseEntity<PracticeRooms> addPracticeRoom(PracticeRoomsRequestDTO practiceRoomsRequestDTO) {
        PracticeRooms practiceRooms = new PracticeRooms();
        // Map DTO fields to PracticeRooms fields
        practiceRooms.setName(practiceRoomsRequestDTO.getName());
        practiceRooms.setImageUrl(practiceRoomsRequestDTO.getImageUrl());
        practiceRooms.setPhone(practiceRoomsRequestDTO.getPhone());
        practiceRooms.setBookingUrl(practiceRoomsRequestDTO.getBookingUrl());
        practiceRooms.setFullAddress(practiceRoomsRequestDTO.getFullAddress());
        practiceRooms.setX(practiceRoomsRequestDTO.getX());
        practiceRooms.setY(practiceRoomsRequestDTO.getY());

        practiceRoomsRepository.save(practiceRooms);
        return ResponseEntity.ok(practiceRooms);
    }

    public ResponseEntity<List<PracticeRooms>> searchPracticeRoomsByAddress(String fullAddress) {
        logger.info("searchPracticeRoomsByAddress: " + fullAddress);
        List<PracticeRooms> practiceRooms = practiceRoomsRepository.findByFullAddressContains(fullAddress);
        return ResponseEntity.ok(practiceRooms);
    }

    public ResponseEntity<PracticeRooms> updatePracticeRooms(long id, PracticeRoomsRequestDTO practiceRoomsRequestDTO){
        PracticeRooms practiceRooms = practiceRoomsRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Invalid practiceRoomsId: " + id)
        );
        practiceRooms.setName(practiceRoomsRequestDTO.getName());
        practiceRooms.setImageUrl(practiceRoomsRequestDTO.getImageUrl());
        practiceRooms.setPhone(practiceRoomsRequestDTO.getPhone());
        practiceRooms.setBookingUrl(practiceRoomsRequestDTO.getBookingUrl());
        practiceRooms.setFullAddress(practiceRoomsRequestDTO.getFullAddress());
        practiceRooms.setX(practiceRoomsRequestDTO.getX());
        practiceRooms.setY(practiceRoomsRequestDTO.getY());

        practiceRoomsRepository.save(practiceRooms);
        return ResponseEntity.ok(practiceRooms);
    }

    public PracticeRoomsWithRoomDataResponseDTO searchPracticeRoomById(Long practiceRoomsId) {
        PracticeRoomsWithRoomDataResponseDTO practiceRoomsWithRoomDataRequestDTO = new PracticeRoomsWithRoomDataResponseDTO();
        List<RoomInfo> roomInfoList = new ArrayList<>();
        PracticeRooms practiceRooms = practiceRoomsRepository.findById(practiceRoomsId).orElseThrow(
                () -> new IllegalArgumentException("Invalid practiceRoomsId: " + practiceRoomsId)
        );

        List<RoomData> roomDataList = roomDataRepository.findByPrId(practiceRooms.getBookingBusinessId());
        practiceRoomsWithRoomDataRequestDTO.setPracticeRoomId(practiceRooms.getId());
        practiceRoomsWithRoomDataRequestDTO.setPracticeRoomName(practiceRooms.getName());
        practiceRoomsWithRoomDataRequestDTO.setRoadAddress(practiceRooms.getRoadAddress());
        practiceRoomsWithRoomDataRequestDTO.setBookingUrl(practiceRooms.getBookingUrl());
        practiceRoomsWithRoomDataRequestDTO.setImageUrl(practiceRooms.getImageUrl());
        if(!practiceRooms.getPhone().isEmpty()) {
            logger.info("practiceRooms.getPhone() != null  phone: " + practiceRooms.getPhone());
            practiceRoomsWithRoomDataRequestDTO.setPhoneNumber(practiceRooms.getPhone());
        } else{
            logger.info("virtualPhone: " + practiceRooms.getVirtualPhone());
            practiceRoomsWithRoomDataRequestDTO.setPhoneNumber(practiceRooms.getVirtualPhone());
        }
        practiceRoomsWithRoomDataRequestDTO.setFullAddress(practiceRooms.getFullAddress());


        // RoomData를 RoomInfo로 변환
        for (RoomData roomData : roomDataList) {
            RoomInfo roomInfo = new RoomInfo();
            roomInfo.setRoomId(roomData.getRoomId());
            roomInfo.setRoomName(roomData.getName());
            roomInfo.setPrice(roomData.getPrice());
            roomInfoList.add(roomInfo);
        }

        // 변환된 RoomInfo 리스트를 DTO에 설정
        practiceRoomsWithRoomDataRequestDTO.setRoomInfoList(roomInfoList);

        return practiceRoomsWithRoomDataRequestDTO;
    }

    public ResponseEntity<PracticeRooms> updatePracticeRoomsRoadAddress(long id, String roadAddress){
        PracticeRooms practiceRooms = practiceRoomsRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Invalid practiceRoomsId: " + id)
        );
        practiceRooms.setRoadAddress(roadAddress);
        practiceRoomsRepository.save(practiceRooms);
        return ResponseEntity.ok(practiceRooms);
    }

    public ResponseEntity<PracticeRooms> updatePracticeRoomsFullAddress(long id, String fullAddress){
        PracticeRooms practiceRooms = practiceRoomsRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Invalid practiceRoomsId: " + id)
        );
        practiceRooms.setFullAddress(fullAddress);
        practiceRoomsRepository.save(practiceRooms);
        return ResponseEntity.ok(practiceRooms);
    }

    public ResponseEntity<PracticeRooms> updatePracticeRoomsPhone(long id, String phone){
        PracticeRooms practiceRooms = practiceRoomsRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Invalid practiceRoomsId: " + id)
        );
        practiceRooms.setPhone(phone);
        practiceRoomsRepository.save(practiceRooms);
        return ResponseEntity.ok(practiceRooms);
    }

    public ResponseEntity<PracticeRooms> updatePracticeRoomsBookingUrl(long id, String bookingUrl){
        PracticeRooms practiceRooms = practiceRoomsRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Invalid practiceRoomsId: " + id)
        );
        practiceRooms.setBookingUrl(bookingUrl);
        practiceRoomsRepository.save(practiceRooms);
        return ResponseEntity.ok(practiceRooms);
    }
}