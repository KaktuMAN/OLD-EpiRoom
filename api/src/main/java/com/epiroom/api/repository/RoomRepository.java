package com.epiroom.api.repository;

import java.util.List;

import com.epiroom.api.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByCampusCode(String code);
    Room findByCampusCodeAndCode(String campusCode, String code);
}