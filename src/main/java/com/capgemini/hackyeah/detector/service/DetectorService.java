package com.capgemini.hackyeah.detector.service;

import com.capgemini.hackyeah.detector.model.DetectorCreateRequest;
import com.capgemini.hackyeah.detector.model.DetectorDto;
import com.capgemini.hackyeah.domain.model.Detector;
import com.capgemini.hackyeah.domain.repository.CollectorRepository;
import com.capgemini.hackyeah.domain.repository.DetectorRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DetectorService {

    private final DetectorRepository repository;
    private final CollectorRepository collectorRepository;

    public void registerDetector(DetectorCreateRequest request, Integer collectorId) {
        Detector detector = Detector.builder()
                .collector(collectorRepository.findById(collectorId).orElseThrow(() -> new EntityNotFoundException("Collector not found")))
                .wasteType(request.getWasteType())
                .active(false)
                .build();
        repository.save(detector);
    }

    @Transactional
    public void activateDetector(Integer detectorId) {
        repository.activateDetector(detectorId);
    }

    public List<DetectorDto> getDetectorsForCollector(Integer id) {
        return repository.findAllByCollector(collectorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Collector for detectors not found")))
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private DetectorDto mapToDto(Detector detector) {
        return DetectorDto.builder()
                .id(detector.getId())
                .active(detector.getActive())
                .wasteType(detector.getWasteType())
                .build();
    }
}
