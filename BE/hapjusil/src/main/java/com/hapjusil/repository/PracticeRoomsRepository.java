package com.hapjusil.repository;

import com.hapjusil.domain.PracticeRooms;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PracticeRoomsRepository extends JpaRepository<PracticeRooms, String> {
    List<PracticeRooms> findByNameContaining(String name);
}
