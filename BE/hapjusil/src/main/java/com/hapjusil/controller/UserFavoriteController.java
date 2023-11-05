package com.hapjusil.controller;

import com.hapjusil.domain.PracticeRoom;
import com.hapjusil.domain.UserFavorite;
import com.hapjusil.service.UserFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-favorites")
public class UserFavoriteController {

    @Autowired
    private UserFavoriteService userFavoriteService;

    @PostMapping("/add/{userId}/{practiceRoomId}")
    public ResponseEntity<UserFavorite> addUserFavorite(@PathVariable Long userId, @PathVariable Long practiceRoomId) {
        UserFavorite userFavorite = userFavoriteService.addUserFavorite(userId, practiceRoomId);
        return ResponseEntity.ok(userFavorite);
    }

    @GetMapping("/{userId}")
    public List<PracticeRoom> getFavoriteRooms(@PathVariable Long userId) {
        return userFavoriteService.findFavoriteRoomsByUserId(userId);
    }
}