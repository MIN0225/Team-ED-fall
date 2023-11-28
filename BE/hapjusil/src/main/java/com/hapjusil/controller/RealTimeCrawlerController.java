package com.hapjusil.controller;

import com.hapjusil.dto.AvailableRoom2Dto;
import com.hapjusil.dto.CrawlerResultDto;
import com.hapjusil.service.RealTimeCrawlerService;
import com.hapjusil.service.RealTimeCrawlerService2;
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
    private RealTimeCrawlerService2 realTimeCrawlerService2;

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
    public ResponseEntity<Map<String, List<AvailableRoom2Dto>>> getCrawlerResult2(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                                                                  @RequestParam @DateTimeFormat(pattern = "HH:mm:ss") LocalTime startTime,
                                                                                  @RequestParam @DateTimeFormat(pattern = "HH:mm:ss") LocalTime endTime,
                                                                                  @RequestParam String gu) {

        LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(date, endTime);

        try {
            Map<String, List<AvailableRoom2Dto>> results = realTimeCrawlerService2.getAvailableRoomsWithCrawler2(startDateTime, endDateTime, gu);
            logger.info("results: {}", results);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            logger.error("error: {}", e);
            return ResponseEntity.badRequest().body(null);
        }
    }
}