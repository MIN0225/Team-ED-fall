package com.hapjusil.controller;

import com.amazonaws.services.dynamodbv2.model.Get;
import com.hapjusil.domain.PracticeRoom;
import com.hapjusil.domain.PracticeRooms;
import com.hapjusil.domain.UserFavorite;
import com.hapjusil.repository.PracticeRoomRepository;
import com.hapjusil.repository.PracticeRoomsRepository;
import com.hapjusil.repository.UserFavoriteRepository;
import com.hapjusil.service.UserFavoriteService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/user-favorites")
public class UserFavoriteController {

    @Autowired
    private UserFavoriteService userFavoriteService;

    @Autowired
    private PracticeRoomRepository practiceRoomRepository;

    @Autowired
    private PracticeRoomsRepository practiceRoomsRepository;

    @PostMapping("/add/{userId}")
    public ResponseEntity<UserFavorite> addUserFavorite( // 합주실 찜
            @PathVariable Long userId,
            @RequestParam(required = false) Long practiceRoomId,
            @RequestParam(required = false) String practiceRoomsId) {
        UserFavorite userFavorite = userFavoriteService.addUserFavorite(userId, practiceRoomId, practiceRoomsId);
        return ResponseEntity.ok(userFavorite);
    }

    @GetMapping("/{userId}")
    public List<Object> getFavoriteRooms(@PathVariable Long userId) { // 사용자가 찜한 합주실 조회
        return userFavoriteService.findFavoriteRoomsByUserId(userId);
    }

    //테스트용(지울거)
    @GetMapping("/test/{practiceRoomId}")
    public Optional<PracticeRoom> getTest(@PathVariable Long practiceRoomId) {
        Logger logger = org.slf4j.LoggerFactory.getLogger(Get.class);
        logger.info("practiceRoomId: " + practiceRoomId);
        return practiceRoomRepository.findById(practiceRoomId);
    }
}