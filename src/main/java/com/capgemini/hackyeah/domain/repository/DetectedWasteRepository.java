package com.capgemini.hackyeah.domain.repository;

import com.capgemini.hackyeah.domain.model.DetectedWaste;
import com.capgemini.hackyeah.domain.model.Detector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Repository
public interface DetectedWasteRepository extends JpaRepository<DetectedWaste, Integer> {

    DetectedWaste findFirstByDetectorAndTimestampIsBetween(Detector detector, LocalDateTime before, LocalDateTime after);

}
