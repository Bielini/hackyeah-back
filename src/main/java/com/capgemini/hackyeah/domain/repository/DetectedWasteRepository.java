package com.capgemini.hackyeah.domain.repository;

import com.capgemini.hackyeah.domain.model.DetectedWaste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetectedWasteRepository extends JpaRepository<DetectedWaste, Integer> {
}
