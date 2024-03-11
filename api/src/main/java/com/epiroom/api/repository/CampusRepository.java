package com.epiroom.api.repository;

import com.epiroom.api.model.Campus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampusRepository extends JpaRepository<Campus, Long> {
    Campus findByCode(String code);
}
