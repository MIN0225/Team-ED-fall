package com.hapjusil.controller;

import com.hapjusil.dto.AvailableRoom2Dto;
import com.hapjusil.dto.AvailableRoom3Dto;
import com.hapjusil.dto.CrawlerResultDto;
import com.hapjusil.service.RealTimeCrawlerService;
import com.hapjusil.service.RealTimeCrawlerService2;
import com.hapjusil.service.RealTimeCrawlerService3;
import com.hapjusil.service.RealTimeCrawlerService4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/realtime-crawler")
@CrossOrigin("*")
public class RealTimeCrawlerController {

    @Autowired
    private RealTimeCrawlerService realTimeCrawlerService;

    @Autowired
    private RealTimeCrawlerService3 realTimeCrawlerService3;

    @Autowired
    private RealTimeCrawlerService4 realTimeCrawlerService4;

    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @PostMapping("/")
    public ResponseEntity<CrawlerResultDto[]> startCrawler(@RequestBody StartController.CrawlerRequest request) {
        try {
            CrawlerResultDto[] results = realTimeCrawlerService.runCrawler(request.getCommonAddress(), request.getDate());
            logger.info("results: {}", results);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            logger.error("error: {}", e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/available-rooms")
    public ResponseEntity<List<AvailableRoom2Dto>> getCrawlerResult(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                                    @RequestParam @DateTimeFormat(pattern = "HH:mm:ss") LocalTime startTime,
                                                    @RequestParam @DateTimeFormat(pattern = "HH:mm:ss") LocalTime endTime,
                                                    @RequestParam String gu) {

        LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(date, endTime);

        try {
            List<AvailableRoom2Dto> results = realTimeCrawlerService.getAvailableRoomsWithCrawler(startDateTime, endDateTime, gu);
            logger.info("results: {}", results);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            logger.error("error: {}", e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/available-rooms2")
    public ResponseEntity<List<AvailableRoom3Dto>> getCrawlerResult2(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                                                                  @RequestParam @DateTimeFormat(pattern = "HH:mm:ss") LocalTime startTime,
                                                                                  @RequestParam @DateTimeFormat(pattern = "HH:mm:ss") LocalTime endTime,
                                                                                  @RequestParam String gu) {

        LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(date, endTime);

        try {
            List<AvailableRoom3Dto> results = realTimeCrawlerService3.getAvailableRoomsWithCrawler3(startDateTime, endDateTime, gu);
            logger.info("results: {}", results);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            logger.error("error: {}", e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/available-rooms3") // 은학님 로컬pc에서 크롤러 돌린 데이터로 처리
    public ResponseEntity<List<AvailableRoom3Dto>> getCrawlerResult3(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                                                     @RequestParam String startTimeString, // LocalTime 대신 String으로 받음
                                                                     @RequestParam String endTimeString,   // LocalTime 대신 String으로 받음
                                                                     @RequestParam String gu) {

        LocalTime startTime = parseToLocalTime(startTimeString);
        LocalTime endTime = parseToLocalTime(endTimeString);

        LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(date, endTime);

        if ("24:00:00".equals(endTimeString) || "00:00:00".equals(endTimeString)) {
            endDateTime = LocalDateTime.of(date.plusDays(1), LocalTime.MIDNIGHT);
        }

        try {
            List<AvailableRoom3Dto> results = realTimeCrawlerService4.getAvailableRoomsWithCrawler4(startDateTime, endDateTime, gu);
            logger.info("results: {}", results);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            logger.error("error: {}", e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    private LocalTime parseToLocalTime(String timeString) {
        if ("24:00:00".equals(timeString) || "00:00:00".equals(timeString)) {
            return LocalTime.MIDNIGHT; // 자정 (00:00:00)으로 설정
        }
        return LocalTime.parse(timeString); // 일반적인 시간은 그대로 파싱
    }
}