package com.epiroom.api.repository;

import com.epiroom.api.model.Floor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FloorRepository extends JpaRepository<Floor, Long> {
    Floor findById(int id);

    Floor findByCampusCodeAndFloor(String campusCode, int floor);

    boolean existsByCampusCodeAndFloor(String campusCode, int floor);
}