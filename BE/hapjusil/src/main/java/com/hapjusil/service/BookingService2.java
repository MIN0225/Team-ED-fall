package com.hapjusil.service;

import com.hapjusil.domain.PrHasBooking;
import com.hapjusil.domain.ReservationData;
import com.hapjusil.domain.RoomData;
import com.hapjusil.dto.AvailableRoom3Dto;
import com.hapjusil.dto.RoomInfo;
import com.hapjusil.repository.PrHasBookingRepository;
import com.hapjusil.repository.ReservationDataRepository;
import com.hapjusil.repository.RoomDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingService2 { // AvailableRoom3Dto를 위해 만듦

    private static final Logger logger = LoggerFactory.getLogger(BookingService2.class);

    @Autowired
    private ReservationDataRepository reservationDataRepository;

    @Autowired
    private PrHasBookingRepository prHasBookingRepository;

    @Autowired
    private RoomDataRepository roomDataRepository;  // RoomDataRepository 주입

    private Map<Long, List<List<ReservationData>>> groupContinuousReservations(List<ReservationData> reservations) {
        Map<Long, List<List<ReservationData>>> groupedReservations = new HashMap<>(); // 새로운 해시맵 생성. 반환될 맵
        for (ReservationData reservation : reservations) { // 날짜 하루에 있는 reservationData들을 reservation에 하나씩 넣음
            Long roomId = reservation.getRoomId().longValue();
            if (!groupedReservations.containsKey(roomId)) {
                groupedReservations.put(roomId, new ArrayList<>());
                // New room ID found, initialize with a list containing the current reservation
                List<ReservationData> initialList = new ArrayList<>();
                initialList.add(reservation);
                groupedReservations.get(roomId).add(initialList);
            } else {
                // Get the list of continuous slots for this room ID
                List<List<ReservationData>> roomReservationsLists = groupedReservations.get(roomId);
                // Get the last list of continuous slots
                List<ReservationData> lastList = roomReservationsLists.get(roomReservationsLists.size() - 1);
                LocalTime lastTime = lastList.get(lastList.size() - 1).getAvailableTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
                LocalTime currentTime = reservation.getAvailableTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
                if (lastTime.plusHours(1).equals(currentTime)) {
                    // Continuous slot found, add to the last list
                    lastList.add(reservation);
                } else {
                    // New continuous slot found, create a new list and add to roomReservationsLists
                    List<ReservationData> newList = new ArrayList<>();
                    newList.add(reservation);
                    roomReservationsLists.add(newList);
                }
            }
        }
        return groupedReservations;
    }

    private boolean isContinuousSlot(List<ReservationData> reservations, LocalDateTime startTime, LocalDateTime endTime) {
        if (reservations == null || reservations.isEmpty()) {
            logger.warn("예약 목록이 null이거나 비어 있음");  // 로그 추가
            return false;
        }

        // 예약을 availableTime 오름차순으로 정렬하여 처리를 쉽게 합니다.
        reservations.sort((r1, r2) -> r1.getAvailableTime().compareTo(r2.getAvailableTime()));

        // startTime과 endTime 사이의 모든 시간 슬롯을 확인하며 예약이 있는지 확인합니다.
        LocalDateTime checkTime = startTime;
        while (!checkTime.isAfter(endTime.minusHours(1))) {  // endTime은 포함되지 않으므로 endTime.minusHours(1)을 사용합니다.
            boolean reserved = false;
            for (ReservationData reservation : reservations) {
                LocalDateTime reservationTime = reservation.getAvailableTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                if (checkTime.equals(reservationTime)) {
                    reserved = true;
                    break;
                }
            }
            if (!reserved) {
                // 예약이 없는 시간 슬롯을 찾았으므로 false를 반환합니다.
//                logger.warn("예약이 없는 시간 슬롯 발견: {}", checkTime);  // 로그 추가
                return false;
            }
            // 다음 시간 슬롯을 확인합니다.
            checkTime = checkTime.plusHours(1);
        }

        return true;
    }


    public List<AvailableRoom3Dto> getAvailableRooms3(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        // LocalDateTime을 Date 객체로 변환합니다.
        Date startDate = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());

        // 로그를 출력하여 사용자에게 검색 범위를 알립니다.
        logger.info("입력된 날짜 범위 {} 와 {} 사이의 예약 가능한 방을 검색합니다.", startDate, endDate);

        // 주어진 날짜에 대한 모든 예약을 찾습니다.
        List<ReservationData> allReservations = reservationDataRepository.findByDate(startDate);
        logger.info("해당 날짜에 {}개의 allReservations.size()가 있습니다.", allReservations.size());

        // 예약을 그룹화합니다.
        Map<Long, List<List<ReservationData>>> groupedReservations = groupContinuousReservations(allReservations);

        // 합주실 ID별로 연습실 정보를 저장할 맵
        Map<Long, List<RoomData>> practiceRoomToRoomsMap = new HashMap<>();

        for (Map.Entry<Long, List<List<ReservationData>>> entry : groupedReservations.entrySet()) {
            Long prId = entry.getKey();
            for (List<ReservationData> continuousReservations : entry.getValue()) {
                if (isContinuousSlot(continuousReservations, startDateTime, endDateTime)) {
                    RoomData roomData = roomDataRepository.findById(prId).orElse(null);
                    if (roomData != null) {
                        practiceRoomToRoomsMap.computeIfAbsent(roomData.getPrId(), k -> new ArrayList<>()).add(roomData);
                    }
                }
            }
        }

        List<AvailableRoom3Dto> availableRooms = new ArrayList<>();
        for (Map.Entry<Long, List<RoomData>> entry : practiceRoomToRoomsMap.entrySet()) {
            Long prId = entry.getKey();
            List<RoomData> rooms = entry.getValue();

            PrHasBooking prHasBooking = prHasBookingRepository.findByBookingBusinessId(prId).orElse(null);
            if (prHasBooking != null) {
                AvailableRoom3Dto dto = new AvailableRoom3Dto();
                dto.setPracticeRoomId(prId);
                dto.setPracticeRoomName(prHasBooking.getName());
                dto.setAddress(prHasBooking.getAddress());
                dto.setImageUrl(prHasBooking.getImageUrl());
                dto.setVisitorReviewScore(prHasBooking.getVisitorReviewScore());
                dto.setCommonAddress(prHasBooking.getCommonAddress());
                dto.setOrigin(false); // DB에서 가져온 방이므로 origin은 false로 설정합니다.

                List<RoomInfo> roomInfoList = rooms.stream().map(room -> {
                    RoomInfo roomInfo = new RoomInfo();
                    roomInfo.setRoomId(room.getRoomId());
                    roomInfo.setRoomName(room.getName());
                    roomInfo.setPrice(room.getPrice());
                    return roomInfo;
                }).collect(Collectors.toList());

                dto.setRoomInfoList(roomInfoList);
                availableRooms.add(dto);
            }
        }

        logger.info("{}개의 예약 가능한 방을 반환합니다.", availableRooms.size());
        return availableRooms;
    }
}