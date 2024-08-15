package com.epiroom.api.repository;

import com.epiroom.api.model.Floor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FloorRepository extends JpaRepository<Floor, Long> {
    List<Floor> findAllByCampusCode(String campusCode);
}