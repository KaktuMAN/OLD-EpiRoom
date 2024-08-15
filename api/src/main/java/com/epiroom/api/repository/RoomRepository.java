package com.epiroom.api.repository;

import com.epiroom.api.model.Campus;
import com.epiroom.api.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findByCampusAndCode(Campus campus, String code);
    List<Room> findAllByCampusCode(String campusCode);
    List<Room> findAllByCampusCodeAndCodeIn(String campusCode, List<String> codes);
    Room findById(int id);
}