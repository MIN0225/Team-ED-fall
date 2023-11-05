package com.hapjusil.repository;

import com.hapjusil.domain.RoomData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomDataRepository extends JpaRepository<RoomData, String> {

}
