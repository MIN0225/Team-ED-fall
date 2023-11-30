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
public class RealTimeCrawlerService {
    @Autowired
    private ReservationDataRepository reservationDataRepository;

    @Autowired
    private PrHasBookingRepository prHasBookingRepository;
    @Autowired
    private RoomDataRepository roomDataRepository;

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


    public List<AvailableRoom2Dto> getAvailableRooms2(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        // LocalDateTime을 Date 객체로 변환합니다.
        Date startDate = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());

        // 로그를 출력하여 사용자에게 검색 범위를 알립니다.
        logger.info("입력된 날짜 범위 {} 와 {} 사이의 예약 가능한 방을 검색합니다.", startDate, endDate);

        // 주어진 날짜에 대한 모든 예약을 찾습니다.
        List<ReservationData> allReservations = reservationDataRepository.findByDate(startDate);
        logger.info("해당 날짜에 {}개의 예약이 있습니다.", allReservations.size());

        // 예약을 그룹화합니다.
        Map<String, List<List<ReservationData>>> groupedReservations = groupContinuousReservations(allReservations);

        // 합주실 ID별로 연습실 정보를 저장할 맵
        Map<String, List<RoomData>> practiceRoomToRoomsMap = new HashMap<>();

        for (Map.Entry<String, List<List<ReservationData>>> entry : groupedReservations.entrySet()) {
            String prId = entry.getKey(); // prId를 얻는다
            List<List<ReservationData>> continuousReservationsLists = entry.getValue();
            for (List<ReservationData> continuousReservations : continuousReservationsLists) {
                List<String> times = continuousReservations.stream()
                        .map(reservation -> reservation.getAvailableTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                        .collect(Collectors.toList());
                if (isContinuousSlot(times, startDateTime, endDateTime)) {
                    RoomData roomData = roomDataRepository.findById(prId).orElse(null);
                    if (roomData != null) {
                        practiceRoomToRoomsMap.computeIfAbsent(roomData.getPrId(), k -> new ArrayList<>()).add(roomData);
                    }
                }
            }
        }

        List<AvailableRoom2Dto> availableRooms = new ArrayList<>();
        for (Map.Entry<String, List<RoomData>> entry : practiceRoomToRoomsMap.entrySet()) {
            String prId = entry.getKey();
            List<RoomData> rooms = entry.getValue();

            PrHasBooking prHasBooking = prHasBookingRepository.findByBookingBusinessId(prId).orElse(null);
            if (prHasBooking != null) {
                AvailableRoom2Dto dto = new AvailableRoom2Dto();
                dto.setPracticeRoomId(prId);
                dto.setPracticeRoomName(prHasBooking.getName());
                dto.setAddress(prHasBooking.getAddress());
                dto.setImageUrl(prHasBooking.getImageUrl());

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

    public List<AvailableRoom2Dto> getAvailableRoomsWithCrawler(LocalDateTime startDateTime, LocalDateTime endDateTime, String gu) throws IOException, InterruptedException {
        logger.info("입력한 구: {}", gu);
        Date startDate = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());

        // 구 이름을 쉼표로 구분하여 리스트로 변환
        List<String> guList = Arrays.asList(gu.split("\\s*,\\s*"));
        logger.info("구 리스트: {}", guList);

        String startDateString = startDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        logger.info("시작시간: {}", startDateString);

        // 각 구 이름에 해당하는 주소 패턴 매핑
//        List<String> addressPatterns = guList.stream() // 망원, 연남, 합정 한번에 입력한 경우를 위함
//                .map(g -> g + "동")
//                .collect(Collectors.toList());

        List<CrawlerResultDto> allCrawlerResults = new ArrayList<>();


        for (String individualGu : guList) {
            logger.info("개별 구: {}", individualGu);
            CrawlerResultDto[] crawlerResults = runCrawler(individualGu, startDateString);
            logger.info("for(String individualGu : guList) 크롤링 결과: {}", Arrays.toString(crawlerResults));
            allCrawlerResults.addAll(Arrays.asList(crawlerResults));
        }

        Map<String, AvailableRoom2Dto> roomDtoMap = new HashMap<>();

        for (CrawlerResultDto result : allCrawlerResults) {
            PrHasBooking prHasBooking = prHasBookingRepository.findByBookingBusinessId(result.getPrId()).orElse(null);
//            logger.info("가능한 방 PrHasBooking prHasBooking: {}", prHasBooking.toString()); // prHasBooking 이상없음

            if (prHasBooking != null) {
                logger.info("prHasBooking.getCommonAddress(): {}", prHasBooking.getCommonAddress());
                logger.info("prHasBooking.getCommonAddress() 타입: {}", prHasBooking.getCommonAddress().getClass().getName());
                logger.info("prHasBooking.getCommonAddress().contains(gu): {}", prHasBooking.getCommonAddress().contains(gu));
                // 주소가 입력된 구 리스트 중 하나와 일치하는지 확인
//                boolean addressMatches = addressPatterns.stream()
//                        .anyMatch(pattern -> prHasBooking.getCommonAddress().contains(pattern));
//                if (addressMatches) {
//
//                }
                List<String> availableTimes = result.getData();
                if (availableTimes != null && isContinuousSlot(availableTimes, startDateTime, endDateTime)) {
                    roomDtoMap.computeIfAbsent(result.getPrId(), k -> createInitialAvailableRoom2Dto(prHasBooking))
                            .getRoomInfoList()
                            .add(createRoomInfo(result));
                }

            }
        }

        logger.info("총 예약 가능한 방의 수: {}", roomDtoMap.values().size());
        return new ArrayList<>(roomDtoMap.values());
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