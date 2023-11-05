package com.hapjusil.repository;

import com.hapjusil.domain.ReservationData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface ReservationDataRepository extends JpaRepository<ReservationData, ReservationData.ReservationDataId> {

    @Query("SELECT rd FROM ReservationData rd WHERE DATE(rd.availableTime) = :date")
    List<ReservationData> findByDate(@Param("date") Date startTime);

}
