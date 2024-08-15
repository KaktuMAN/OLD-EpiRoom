package com.epiroom.api.repository;

import com.epiroom.api.model.Activity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findAllByCampusCode(Pageable pageable, String campusCode);
    Activity findByIdAndCampusCode(int id, String campusCode);
    boolean existsById(int id);
}