package com.hapjusil.repository;

import com.hapjusil.domain.RoomData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomDataRepository extends JpaRepository<RoomData, Long> {
    @Query("SELECT r FROM RoomData r WHERE r.roomId = :roomId")
    Optional<RoomData> findByRoomId(Long roomId);


    List<RoomData> findByPrId(Long practiceRoomId);
}
