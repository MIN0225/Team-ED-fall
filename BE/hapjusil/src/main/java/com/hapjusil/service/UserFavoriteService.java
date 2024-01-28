package com.hapjusil.service;

import com.hapjusil.domain.PracticeRoom;
import com.hapjusil.domain.PracticeRooms;
import com.hapjusil.domain.User;
import com.hapjusil.domain.UserFavorite;
import com.hapjusil.repository.PracticeRoomRepository;
import com.hapjusil.repository.PracticeRoomsRepository;
import com.hapjusil.repository.UserFavoriteRepository;
import com.hapjusil.repository.UserRepository;
import org.aspectj.bridge.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserFavoriteService {

    @Autowired
    private UserFavoriteRepository userFavoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PracticeRoomRepository practiceRoomRepository;

    @Autowired
    private PracticeRoomsRepository practiceRoomsRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserFavoriteService.class.getName());


    @Transactional
    public UserFavorite addUserFavorite(Long userId, Long practiceRoomsId) {
        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        LOGGER.info("User found with ID: {} practiceRoomsId: {}", userId, practiceRoomsId);

        // practiceRoomsId가 제공된 경우
        if (practiceRoomsId != null) {
            LOGGER.info("Looking for PracticeRooms with ID: " + practiceRoomsId);
            PracticeRooms practiceRooms = practiceRoomsRepository.findById(practiceRoomsId)
                    .orElseThrow(() -> new RuntimeException("PracticeRooms not found"));
            LOGGER.info("PracticeRooms found with ID: " + practiceRoomsId);
            return handlePracticeRoomsFavorite(user, practiceRooms);
        }
        // ID가 제공되지 않은 경우
        else {
            LOGGER.info("Either practiceRoomId or practiceRoomsId must be provided");
            throw new IllegalArgumentException("Either practiceRoomId or practiceRoomsId must be provided");
        }
    }

    // 합주실 찜 처리
//    private UserFavorite handlePracticeRoomFavorite(User user, PracticeRoom practiceRoom) {
//        // 기존 찜 확인
//        Optional<UserFavorite> existingFavorite = userFavoriteRepository.findByUserAndPracticeRoom(user, practiceRoom);
//        if (existingFavorite.isPresent()) {
//            // 기존 찜 삭제
//            LOGGER.info("Removing existing favorite for PracticeRoom with ID: " + practiceRoom.getId());
//            userFavoriteRepository.delete(existingFavorite.get());
//            return null; // 찜 취소
//        } else {
//            // 새로운 찜 추가
//            LOGGER.info("Adding new favorite for PracticeRoom with ID: " + practiceRoom.getId());
//            UserFavorite userFavorite = new UserFavorite();
//            userFavorite.setUser(user);
//            userFavorite.setPracticeRoom(practiceRoom);
//            return userFavoriteRepository.save(userFavorite); // 찜 추가
//        }
//    }

    // 서울 전체 합주실 찜 처리
    private UserFavorite handlePracticeRoomsFavorite(User user, PracticeRooms practiceRooms) {
        // 기존 찜 확인
        Optional<UserFavorite> existingFavorite = userFavoriteRepository.findByUserAndPracticeRooms(user, practiceRooms);
        if (existingFavorite.isPresent()) {
            // 기존 찜 삭제
            LOGGER.info("Removing existing favorite for PracticeRooms with ID: " + practiceRooms.getId());
            userFavoriteRepository.delete(existingFavorite.get());
            return null; // 찜 취소
        } else {
            // 새로운 찜 추가
            LOGGER.info("Adding new favorite for PracticeRooms with ID: " + practiceRooms.getId());
            UserFavorite userFavorite = new UserFavorite();
            userFavorite.setUser(user);
            userFavorite.setPracticeRooms(practiceRooms);
            return userFavoriteRepository.save(userFavorite); // 찜 추가
        }
    }


    public List<Object> findFavoriteRoomsByUserId(Long userId) {
        List<UserFavorite> favorites = userFavoriteRepository.findByUserId(userId);
        LOGGER.info("Found {} favorites for user with ID: {}", favorites.size(), userId);
        return favorites.stream()
                .flatMap(userFavorite -> userFavorite.getSelectedPracticeRoom().stream())
                .collect(Collectors.toList());
    }
}