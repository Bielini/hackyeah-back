package com.capgemini.hackyeah.domain.repository;

import com.capgemini.hackyeah.domain.model.Collector;
import com.capgemini.hackyeah.domain.model.Detector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetectorRepository extends JpaRepository<Detector, Integer> {

    @Query("UPDATE Detector d SET d.active=true WHERE d.id=:detectorId")
    @Modifying
    void activateDetector(Integer detectorId);

    List<Detector> findAllByCollector(Collector collector);
}
