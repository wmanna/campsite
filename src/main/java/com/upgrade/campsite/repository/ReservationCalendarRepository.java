package com.upgrade.campsite.repository;

import com.upgrade.campsite.entity.ReservationCalendar;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationCalendarRepository extends CrudRepository<ReservationCalendar, String> {

    List<ReservationCalendar> findByCalendarDateGreaterThanEqualAndCalendarDateLessThanEqual(LocalDate beginDate, LocalDate endDate);
    void deleteByReservationCode(String reservationCode);
}