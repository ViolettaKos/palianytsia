package com.example.palianytsia.repository;

import com.example.palianytsia.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Location findLocationByUser_Id(Long user_id);

    Location findLocationById(Long location_id);
}
