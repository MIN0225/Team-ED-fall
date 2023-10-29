package com.hapjusil.repository;

import com.hapjusil.domain.UserFavorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserFavoriteRepository extends JpaRepository<UserFavorite, Long> {
    List<UserFavorite> findByUserId(Long userId);
}
