package com.hapjusil.controller;

import com.hapjusil.domain.UserFavorite;
import com.hapjusil.service.UserFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-favorites")
public class UserFavoriteController {

    @Autowired
    private UserFavoriteService userFavoriteService;

    @PostMapping("/add")
    public ResponseEntity<UserFavorite> addUserFavorite(@RequestParam Long userId, @RequestParam Long practiceRoomId) {
        UserFavorite userFavorite = userFavoriteService.addUserFavorite(userId, practiceRoomId);
        return ResponseEntity.ok(userFavorite);
    }
}