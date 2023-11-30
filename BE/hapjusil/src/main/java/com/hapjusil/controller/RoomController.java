package com.hapjusil.controller;

import com.hapjusil.domain.PrHasBooking;
import com.hapjusil.dto.AvailableRoom2Dto;
import com.hapjusil.dto.AvailableRoomDto;
import com.hapjusil.repository.PrHasBookingRepository;
import com.hapjusil.service.BookingService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private BookingService bookingService;

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(RoomController.class);

    @GetMapping("/available")
    public List<AvailableRoomDto> getAvailableRooms( // 날짜, 시작 시간, 종료시간 입력시 예약가능한 합주실 조회
                                                     @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                                     @RequestParam @DateTimeFormat(pattern = "HH:mm:ss") LocalTime startTime,
                                                     @RequestParam @DateTimeFormat(pattern = "HH:mm:ss") LocalTime endTime) {

        LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(date, endTime);

        return bookingService.getAvailableRooms(startDateTime, endDateTime);
    }

    @GetMapping("/available/location")
    public List<AvailableRoomDto> getAvailableRoomsWithGu( // 날짜, 시작 시간, 종료시간, 특정 구 입력시 예약가능한 합주실 조회
                                                           @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                                           @RequestParam @DateTimeFormat(pattern = "HH:mm:ss") LocalTime startTime,
                                                           @RequestParam @DateTimeFormat(pattern = "HH:mm:ss") LocalTime endTime,
                                                           @RequestParam String gu) {

        LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(date, endTime);

        return bookingService.getAvailableRoomsWithGu(startDateTime, endDateTime, gu);
    }

    @GetMapping("/available2")
    public List<AvailableRoom2Dto> getAvailableRooms2( // 날짜, 시작 시간, 종료시간 입력시 예약가능한 합주실 조회
                                                       @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                                       @RequestParam @DateTimeFormat(pattern = "HH:mm:ss") LocalTime startTime,
                                                       @RequestParam @DateTimeFormat(pattern = "HH:mm:ss") LocalTime endTime) {

        LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(date, endTime);

        return bookingService.getAvailableRooms2(startDateTime, endDateTime);
    }

    @GetMapping("/available/location2")
    public List<AvailableRoom2Dto> getAvailableRoomsWithGu2(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam String startTimeString, // LocalTime 대신 String으로 받음
            @RequestParam String endTimeString,   // LocalTime 대신 String으로 받음
            @RequestParam String gu) {

        // LocalTime으로 변환
        LocalTime startTime = parseToLocalTime(startTimeString);
        LocalTime endTime = parseToLocalTime(endTimeString);

        LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(date, endTime);

        // endTimeString이 "24:00:00" 또는 "00:00:00"인 경우 날짜를 하루 늘림
        if ("24:00:00".equals(endTimeString) || "00:00:00".equals(endTimeString)) {
            endDateTime = LocalDateTime.of(date.plusDays(1), LocalTime.MIDNIGHT);
        }

        logger.info("startDateTime: {} endDateTime: {} ", startDateTime, endDateTime);
        return bookingService.getAvailableRoomsWithGu2(startDateTime, endDateTime, gu);
    }

    // LocalTime으로 변환하는 메소드
    private LocalTime parseToLocalTime(String timeString) {
        if ("24:00:00".equals(timeString) || "00:00:00".equals(timeString)) {
            return LocalTime.MIDNIGHT; // 자정 (00:00:00)으로 설정
        }
        return LocalTime.parse(timeString); // 일반적인 시간은 그대로 파싱
    }
}