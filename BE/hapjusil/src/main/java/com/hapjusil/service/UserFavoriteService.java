package com.hapjusil.service;

import com.hapjusil.domain.PracticeRoom;
import com.hapjusil.domain.User;
import com.hapjusil.domain.UserFavorite;
import com.hapjusil.repository.PracticeRoomRepository;
import com.hapjusil.repository.UserFavoriteRepository;
import com.hapjusil.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserFavoriteService {

    @Autowired
    private UserFavoriteRepository userFavoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PracticeRoomRepository practiceRoomRepository;

    @Transactional
    public UserFavorite addUserFavorite(Long userId, Long practiceRoomId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        PracticeRoom practiceRoom = practiceRoomRepository.findById(practiceRoomId).orElseThrow(() -> new RuntimeException("PracticeRoom not found"));

        UserFavorite userFavorite = new UserFavorite();
        userFavorite.setUser(user);
        userFavorite.setPracticeRoom(practiceRoom);
        return userFavoriteRepository.save(userFavorite);
    }
}