package com.hapjusil.repository;

import com.hapjusil.domain.PrHasBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrHasBookingRepository extends JpaRepository<PrHasBooking, String> {
    @Query("SELECT p FROM PrHasBooking p WHERE p.id NOT IN (:bookedRoomIds)")
    List<PrHasBooking> findAllByIdNotIn(@Param("bookedRoomIds") List<String> bookedRoomIds);

    Optional<PrHasBooking> findByBookingBusinessId(String bookingBusinessId);

    @Query("SELECT p FROM PrHasBooking p WHERE p.roadAddress LIKE %:roadAddress%")
    List<PrHasBooking> findByRoadAddressGu(@Param("roadAddress") String gu);
}

