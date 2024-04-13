package com.epiroom.api.repository;

import com.epiroom.api.model.Campus;
import com.epiroom.api.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findByCampusAndCode(Campus campus, String code);
}