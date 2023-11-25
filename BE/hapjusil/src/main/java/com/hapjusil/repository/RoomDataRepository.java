package com.hapjusil.repository;

import com.hapjusil.domain.RoomData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomDataRepository extends JpaRepository<RoomData, String> {
    @Query("SELECT r FROM RoomData r WHERE r.roomId = :roomId")
    Optional<RoomData> findByRoomId(String roomId);
}
