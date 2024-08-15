package com.epiroom.api.repository;

import com.epiroom.api.model.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByCampusCode(Pageable pageable, String campusCode);
    List<Event> findAllByCampusCodeAndStartDateGreaterThanEqualAndEndDateLessThanEqual(Pageable pageable, String campusCode, long startDate, long endDate);
    List<Event> findAllByStartGreaterThanEqualAndEndLessThanEqualAndCampusCode(Date start, Date end, String campusCode);
    boolean existsById(int id);
    Event findByIdAndCampusCode(int id, String campusCode);
}
