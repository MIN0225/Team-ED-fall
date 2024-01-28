package com.hapjusil.controller;

import com.hapjusil.domain.User;
import com.hapjusil.repository.UserRepository;
import com.hapjusil.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {
    private final UserRepository userRepository;

    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Autowired
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UserController.class);

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            // 사용자가 인증되지 않았을 때 응답 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access Denied: User is not authenticated.");
        }

        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            logger.info("email: " + email);
            User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            // 다른 예외 상황 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{userId}/toggleOwner")
    public ResponseEntity<User> toggleOwner(@PathVariable Long userId) {
        User updatedUser = userService.toggleOwner(userId);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("{userId}/{practiceRoomsId}")
    public ResponseEntity<User> addFavorite(@PathVariable Long userId, @PathVariable Long practiceRoomsId) {
        User updatedUser = userService.updateUserPracticeRoomsId(userId, practiceRoomsId);
        return ResponseEntity.ok(updatedUser);
    }

}
