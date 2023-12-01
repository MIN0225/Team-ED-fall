package com.hapjusil.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hapjusil.domain.PrHasBooking;
import com.hapjusil.domain.RoomData;
import com.hapjusil.dto.AvailableRoom3Dto;
import com.hapjusil.dto.CrawlerResultDto;
import com.hapjusil.dto.RoomInfo;
import com.hapjusil.repository.PrHasBookingRepository;
import com.hapjusil.repository.RoomDataRepository;
import com.hapjusil.util.CrawlerConfig;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RealTimeCrawlerService3 {

    @Autowired
    private PrHasBookingRepository prHasBookingRepository;
    @Autowired
    private RoomDataRepository roomDataRepository;

    @Autowired
    private BookingService2 bookingService2;

    @Autowired
    private CrawlerConfig crawlerConfig; // Configuration 클래스 주입

    private static final Logger logger = LoggerFactory.getLogger(RealTimeCrawlerService.class);
    public CrawlerResultDto[] runCrawler(String commonAddress, String date) throws IOException, InterruptedException {
//        String crawlerPath = "/Users/macbookpro/Downloads/Team-ED-fall-develop2/crawler";
//        String crawlerPath = "/home/ubuntu/Team-ED-fall/crawler";
        String crawlerPath = crawlerConfig.getPath(); // Configuration에서 경로를 가져옴
        String crawlerScript = "realtime-crawler-parent.js";
        String resultsFilePath = crawlerPath + "/results.json";
        String command = "node " + crawlerScript + " " + commonAddress + " " + date;

        ProcessBuilder builder = new ProcessBuilder(command.split(" "));
        builder.directory(new File(crawlerPath));
        Process process = builder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        process.waitFor();

        // 크롤링 결과 파일을 읽어서 문자열로 반환
        File resultsFile = new File(resultsFilePath);
        String resultsJson = FileUtils.readFileToString(resultsFile, StandardCharsets.UTF_8);

        // JSON 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(resultsJson, CrawlerResultDto[].class);
    }

    public List<AvailableRoom3Dto> getAvailableRoomsWithCrawler3(LocalDateTime startDateTime, LocalDateTime endDateTime, String gu) throws IOException, InterruptedException {
        logger.info("입력한 구: {}", gu);

        // 구 이름을 쉼표로 구분하여 리스트로 변환
        List<String> guList = Arrays.asList(gu.split("\\s*,\\s*"));
        logger.info("구 리스트: {}", guList);

        String startDateString = startDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        logger.info("시작시간: {}", startDateString);

        List<AvailableRoom3Dto> roomsByGu = new ArrayList<>();

        for (String individualGu : guList) {
            logger.info("개별 구: {}", individualGu);
            CrawlerResultDto[] crawlerResults = runCrawler(individualGu, startDateString);
            logger.info("for(String individualGu : guList) 크롤링 결과: {}", Arrays.toString(crawlerResults));

            for (CrawlerResultDto result : crawlerResults) {
                PrHasBooking prHasBooking = prHasBookingRepository.findByBookingBusinessId(result.getPrId()).orElse(null);
                if (prHasBooking != null) {
                    List<String> availableTimes = result.getData();
                    if (availableTimes != null && isContinuousSlot(availableTimes, startDateTime, endDateTime)) {
                        AvailableRoom3Dto roomDto = createInitialAvailableRoom3Dto(prHasBooking);
                        RoomInfo newRoomInfo = createRoomInfo(result);

                        boolean roomFound = false;
                        for (AvailableRoom3Dto existingRoom : roomsByGu) {
                            if (existingRoom.getPracticeRoomId().equals(roomDto.getPracticeRoomId())) {
                                existingRoom.getRoomInfoList().add(newRoomInfo);
                                roomFound = true;
                                break;
                            }
                        }
                        if (!roomFound) {
                            roomDto.getRoomInfoList().add(newRoomInfo);
                            roomsByGu.add(roomDto);
                        }
                    }
                }
            }
        }

        if (roomsByGu.isEmpty()) {
            logger.info("roomsByGu.isEmpty()");
            // 크롤링 결과가 비어있다면 대체 방법으로 DB에서 예약 가능한 합주실 검색
            return getFallbackAvailableRooms(startDateTime, endDateTime, gu);
        }

        return roomsByGu;
    }

    private List<AvailableRoom3Dto> getFallbackAvailableRooms(LocalDateTime startDateTime, LocalDateTime endDateTime, String originalGu) {
        List<AvailableRoom3Dto> result = new ArrayList<>();
        result = bookingService2.getAvailableRooms3(startDateTime, endDateTime); // AvailableRoom3Dto 반환 (평점 추가)
        logger.info("getAvailableRooms3() 결과 result : {}", result);
        result.sort((room1, room2) -> {
            try {
                int score1 = room1.getVisitorReviewScore() != null ? Integer.parseInt(room1.getVisitorReviewScore()) : 0;
                int score2 = room2.getVisitorReviewScore() != null ? Integer.parseInt(room2.getVisitorReviewScore()) : 0;
                return Integer.compare(score2, score1); // 내림차순 정렬
            } catch (NumberFormatException e) {
                return 0; // 숫자로 변환할 수 없는 경우, 순서를 변경하지 않음
            }
        });

        logger.info("평점 순으로 정렬된 결과: {}", result);
        // 상위 2개 합주실만 선택하여 반환
        return result.stream()
                .filter(room -> !room.getCommonAddress().contains(originalGu)) // originalGu에 해당하지 않는 합주실만 필터링
                .limit(2) // 상위 2개만 선택
                .collect(Collectors.toList());
    }

    private boolean isContinuousSlot(List<String> availableTimes, LocalDateTime startTime, LocalDateTime endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withResolverStyle(ResolverStyle.SMART);

        List<LocalDateTime> times = availableTimes.stream()
                .map(timeStr -> parseDateTimeSafe(timeStr, formatter))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        LocalDateTime checkTime = startTime;
        while (!checkTime.isAfter(endTime.minusHours(1))) {
            if (!times.contains(checkTime)) {
                return false;
            }
            checkTime = checkTime.plusHours(1);
        }

        return true;
    }

    private AvailableRoom3Dto createInitialAvailableRoom3Dto(PrHasBooking prHasBooking) {
        AvailableRoom3Dto dto = new AvailableRoom3Dto();
        dto.setPracticeRoomId(prHasBooking.getId());
        dto.setPracticeRoomName(prHasBooking.getName());
        dto.setAddress(prHasBooking.getAddress());
        dto.setImageUrl(prHasBooking.getImageUrl());
        dto.setVisitorReviewScore(prHasBooking.getVisitorReviewScore());
        dto.setRoomInfoList(new ArrayList<>()); // 초기 빈 리스트
        return dto;
    }

    private RoomInfo createRoomInfo(CrawlerResultDto result) {
        RoomInfo roomInfo = new RoomInfo();
        Optional<RoomData> roomDataOptional = roomDataRepository.findByRoomId(result.getRoomId());
        if (roomDataOptional.isPresent()) {
            RoomData roomData = roomDataOptional.get();
            roomInfo.setRoomId(result.getRoomId());
            roomInfo.setRoomName(roomData.getName());
            roomInfo.setPrice(roomData.getPrice());
        } else {
            // RoomData가 존재하지 않는 경우에 대한 처리
            roomInfo.setRoomId(result.getRoomId());
            roomInfo.setRoomName("Unknown");
            roomInfo.setPrice(-1);
        }
        return roomInfo;
    }

    private Optional<LocalDateTime> parseDateTimeSafe(String dateTimeStr, DateTimeFormatter formatter) {
        try {
            return Optional.of(LocalDateTime.parse(dateTimeStr.trim(), formatter));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }
}