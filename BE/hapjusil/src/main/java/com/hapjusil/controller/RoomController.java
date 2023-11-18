package com.hapjusil.controller;

import com.hapjusil.domain.PrHasBooking;
import com.hapjusil.dto.AvailableRoomDto;
import com.hapjusil.repository.PrHasBookingRepository;
import com.hapjusil.service.BookingService;
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

    @GetMapping("/available")
    public List<AvailableRoomDto> getAvailableRooms( // 날짜, 시작 시간, 종료시간 입력시 예약가능한 합주실 조회
                                                     @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                                     @RequestParam @DateTimeFormat(pattern = "HH:mm:ss") LocalTime startTime,
                                                     @RequestParam @DateTimeFormat(pattern = "HH:mm:ss") LocalTime endTime) {

        LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(date, endTime);

        return bookingService.getAvailableRooms(startDateTime, endDateTime);
    }

}