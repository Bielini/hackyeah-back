package com.capgemini.hackyeah.detectedwaste.service;

import com.capgemini.hackyeah.domain.model.DetectedWaste;
import com.capgemini.hackyeah.domain.repository.DetectedWasteRepository;
import com.capgemini.hackyeah.domain.repository.DetectorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class DetectedWasteService {

    private final DetectedWasteRepository repository;
    private final DetectorRepository detectorRepository;

    public void registerDetector(Integer detectorId) {
        DetectedWaste detectedWaste = DetectedWaste.builder()
                .timestamp(LocalDateTime.now())
                .detector(detectorRepository.findById(detectorId).orElseThrow(() -> new EntityNotFoundException("Detector not found")))
                .build();
        repository.save(detectedWaste);
    }

}
