package com.hapjusil.repository;

import com.hapjusil.domain.PracticeRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PracticeRoomRepository extends JpaRepository<PracticeRoom, Long> {
    @Query("SELECT p FROM PracticeRoom p WHERE NOT EXISTS (SELECT r FROM Reservation r WHERE r.room.practiceRoom = p AND r.reservationDate = :date)")
    List<PracticeRoom> findAvailablePracticeRoomsByDate(LocalDate date);

//    @Query("SELECT pr FROM PracticeRoom pr JOIN pr.rooms r WHERE r.id NOT IN (" +
//            "SELECT res.room.id FROM Reservation res WHERE res.reservationDate = :date AND res.startTime <= :startTime AND res.status = 'PENDING')")
//    List<PracticeRoom> findAvailablePracticeRooms(LocalDate date, LocalTime startTime);

    @Query("SELECT pr FROM PracticeRoom pr JOIN pr.rooms r WHERE r.id NOT IN (" +
            "SELECT res.room.id FROM Reservation res WHERE res.reservationDate = :date AND " +
            "(res.startTime < :endTime AND res.endTime > :startTime) AND res.status = 'PENDING')")
    List<PracticeRoom> findAvailablePracticeRooms(LocalDate date, LocalTime startTime, LocalTime endTime);

    Page<PracticeRoom> findByOrderByRateDesc(Pageable pageable);

    Page<PracticeRoom> findAllByOrderByNameAsc(Pageable pageable); // 합주실 이름순으로 정렬

    List<PracticeRoom> findByNameContaining(String name); // 합주실 이름으로 검색

    Optional<PracticeRoom> findById(Long id); // 합주실 id로 검색
}
