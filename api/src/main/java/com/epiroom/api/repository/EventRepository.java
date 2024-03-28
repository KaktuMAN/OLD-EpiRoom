package com.epiroom.api.repository;

import com.epiroom.api.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByStartAfterAndEndBeforeAndCampusCode(Date start, Date end, String campusCode);
    List<Event> findAllByStartAfterAndEndBeforeAndUsers_Mail(Date start, Date end, String mail);
}
