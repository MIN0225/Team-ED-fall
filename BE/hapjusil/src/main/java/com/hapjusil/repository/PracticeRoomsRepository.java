package com.hapjusil.repository;

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

    Optional<PracticeRooms> findById(long id); // 합주실 id로 검색

    List<PracticeRooms> findByFullAddressContains(String fullAddress);

    Optional<PracticeRooms> findByBookingBusinessId(Long bookingBusinessId); // 합주실 id로 검색
}
