package com.hapjusil.repository;

import com.hapjusil.domain.UserFavorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFavoriteRepository extends JpaRepository<UserFavorite, Long> {
}
