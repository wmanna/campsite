package com.upgrade.campsite.repository;

import com.upgrade.campsite.entity.Reservation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, String> {

    Reservation findByCode(String code);

    List<Reservation> findByArrivalDateGreaterThanEqualAndArrivalDateLessThanEqualAndCancellationDateIsNullOrDepartureDateGreaterThanEqualAndDepartureDateLessThanEqualAndCancellationDateIsNull(LocalDateTime arrivalDate1, LocalDateTime departureDate1, LocalDateTime arrivalDate2, LocalDateTime departureDate2);
}