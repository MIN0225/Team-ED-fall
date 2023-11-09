package com.hapjusil.repository;

<<<<<<< HEAD
import com.hapjusil.domain.PracticeRoom;
import com.hapjusil.domain.PracticeRooms;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PracticeRoomsRepository extends JpaRepository<PracticeRooms, String> {
    List<PracticeRooms> findByNameContaining(String name);

    Page<PracticeRooms> findAllByOrderByNameAsc(Pageable pageable);

    Page<PracticeRooms> findByOrderByVisitorReviewScoreDesc(Pageable pageable);

    Optional<PracticeRooms> findById(String id); // 합주실 id로 검색

=======
import com.hapjusil.domain.PracticeRooms;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PracticeRoomsRepository extends JpaRepository<PracticeRooms, String> {
    List<PracticeRooms> findByNameContaining(String name);
>>>>>>> 313b3b1 (Fix: 서울 전체 합주실 이름으로 검색)
}
