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

    @Transactional
    public UserFavorite addUserFavorite(Long userId, Long practiceRoomId) { // 합줏실 찜
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        PracticeRoom practiceRoom = practiceRoomRepository.findById(practiceRoomId).orElseThrow(() -> new RuntimeException("PracticeRoom not found"));

        // 해당 사용자와 연습실 조합으로 이미 찜한 데이터가 있는지 확인
        Optional<UserFavorite> existingFavorite = userFavoriteRepository.findByUserAndPracticeRoom(user, practiceRoom);

        if (existingFavorite.isPresent()) {
            // 이미 찜한 상태라면 데이터를 삭제
            userFavoriteRepository.delete(existingFavorite.get());
            return null; // 또는 적절한 반환 값을 선택
        } else {
            // 찜한 데이터가 없다면 새로 추가
            UserFavorite userFavorite = new UserFavorite();
            userFavorite.setUser(user);
            userFavorite.setPracticeRoom(practiceRoom);
            return userFavoriteRepository.save(userFavorite);
        }
    }

    public List<PracticeRoom> findFavoriteRoomsByUserId(Long userId) {
        List<UserFavorite> favorites = userFavoriteRepository.findByUserId(userId);
        return favorites.stream()
                .map(UserFavorite::getPracticeRoom)
                .collect(Collectors.toList());
    }
}