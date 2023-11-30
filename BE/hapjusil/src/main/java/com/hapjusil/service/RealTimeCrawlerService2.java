package com.hapjusil.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hapjusil.domain.PrHasBooking;
import com.hapjusil.domain.ReservationData;
import com.hapjusil.domain.RoomData;
import com.hapjusil.dto.AvailableRoom2Dto;
import com.hapjusil.dto.CrawlerResultDto;
import com.hapjusil.dto.RoomInfo;
import com.hapjusil.repository.PrHasBookingRepository;
import com.hapjusil.repository.ReservationDataRepository;
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
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RealTimeCrawlerService2 {
    @Autowired
    private ReservationDataRepository reservationDataRepository;

    @Autowired
    private PrHasBookingRepository prHasBookingRepository;
    @Autowired
    private RoomDataRepository roomDataRepository;

    @Autowired
    private BookingService bookingService;

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

    private Map<String, List<List<ReservationData>>> groupContinuousReservations(List<ReservationData> reservations) {
        Map<String, List<List<ReservationData>>> groupedReservations = new HashMap<>(); // 새로운 해시맵 생성. 반환될 맵
        for (ReservationData reservation : reservations) { // 날짜 하루에 있는 reservationData들을 reservation에 하나씩 넣음
            String roomId = String.valueOf(reservation.getRoomId());
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


    public Map<String, List<AvailableRoom2Dto>> getAvailableRoomsWithCrawler2(LocalDateTime startDateTime, LocalDateTime endDateTime, String gu) throws IOException, InterruptedException {
        logger.info("입력한 구: {}", gu);
        Date startDate = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());

        // 구 이름을 쉼표로 구분하여 리스트로 변환
        List<String> guList = Arrays.asList(gu.split("\\s*,\\s*"));
        logger.info("구 리스트: {}", guList);

        String startDateString = startDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        logger.info("시작시간: {}", startDateString);

        Map<String, List<AvailableRoom2Dto>> roomsByGu = new HashMap<>();


        for (String individualGu : guList) {
            logger.info("개별 구: {}", individualGu);
            CrawlerResultDto[] crawlerResults = runCrawler(individualGu, startDateString);
            logger.info("for(String individualGu : guList) 크롤링 결과: {}", Arrays.toString(crawlerResults));

            for (CrawlerResultDto result : crawlerResults) {
                PrHasBooking prHasBooking = prHasBookingRepository.findByBookingBusinessId(result.getPrId()).orElse(null);
                if (prHasBooking != null) {
                    List<String> availableTimes = result.getData();
                    if (availableTimes != null && isContinuousSlot(availableTimes, startDateTime, endDateTime)) {
                        AvailableRoom2Dto roomDto = createInitialAvailableRoom2Dto(prHasBooking);
                        roomDto.getRoomInfoList().add(createRoomInfo(result));
                        roomsByGu.computeIfAbsent(individualGu, k -> new ArrayList<>()).add(roomDto);
                    }
                }
            }
        }


        logger.info("총 예약 가능한 방의 수 roomsByGu.values().size() : {}", roomsByGu.values().size());
        logger.info("총 예약 가능한 방의 수: roomsByGu.values().stream().mapToInt(List::size).sum() : {}", roomsByGu.values().stream().mapToInt(List::size).sum());
        if (roomsByGu.isEmpty()) {
            logger.info("roomDtoMap.values().size() == 0");
            // 크롤링 결과가 비어있다면 대체 방법으로 DB에서 예약 가능한 합주실 검색
            return getFallbackAvailableRooms(startDateTime, endDateTime, gu);
        }

        return roomsByGu;
    }

    private Map<String, List<AvailableRoom2Dto>> getFallbackAvailableRooms(LocalDateTime startDateTime, LocalDateTime endDateTime, String originalGu) {
        List<String> fallbackDistricts = new ArrayList<>(Arrays.asList("연남동", "합정동", "강서구", "성동구", "서초구", "동작구", "송파구", "종로구", "광진구", "서초구", "은평구"));
        fallbackDistricts.remove(originalGu); // 원래 구를 대체하기 위해 리스트에서 제거
        Collections.shuffle(fallbackDistricts); // 구 목록을 무작위로 섞음

        Map<String, List<AvailableRoom2Dto>> roomsByGu = new HashMap<>();
        int foundCount = 0;

        for (String gu : fallbackDistricts) {
            List<AvailableRoom2Dto> roomsInGu = bookingService.getAvailableRoomsWithGu2(startDateTime, endDateTime, gu);
            if (roomsInGu != null && !roomsInGu.isEmpty()) {
                roomsByGu.put(gu, roomsInGu);
                foundCount++;
                if (foundCount == 2) {
                    break; // 2개의 결과를 찾으면 반복을 종료
                }
            }
        }

        return roomsByGu;
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

    private AvailableRoom2Dto createInitialAvailableRoom2Dto(PrHasBooking prHasBooking) {
        AvailableRoom2Dto dto = new AvailableRoom2Dto();
        dto.setPracticeRoomId(prHasBooking.getId());
        dto.setPracticeRoomName(prHasBooking.getName());
        dto.setAddress(prHasBooking.getAddress());
        dto.setImageUrl(prHasBooking.getImageUrl());
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