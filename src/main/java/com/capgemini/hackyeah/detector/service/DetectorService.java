package com.capgemini.hackyeah.detector.service;

import com.capgemini.hackyeah.auth.service.AuthenticationService;
import com.capgemini.hackyeah.detector.model.DetectorCreateRequest;
import com.capgemini.hackyeah.detector.model.DetectorDto;
import com.capgemini.hackyeah.domain.model.Detector;
import com.capgemini.hackyeah.domain.model.User;
import com.capgemini.hackyeah.domain.model.WasteType;
import com.capgemini.hackyeah.domain.repository.CollectorRepository;
import com.capgemini.hackyeah.domain.repository.DetectedWasteRepository;
import com.capgemini.hackyeah.domain.repository.DetectorRepository;
import com.capgemini.hackyeah.domain.repository.UserRepository;
import com.capgemini.hackyeah.point.service.PointService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class DetectorService {

    private final DetectorRepository repository;
    private final CollectorRepository collectorRepository;
    private final DetectedWasteRepository detectedWasteRepository;
    private final PointService pointService;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

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

    public boolean getDetectorStatus(WasteType wasteType, Integer collectorId) {
        Detector detector = repository.findAllByCollector(collectorRepository.findById(collectorId).orElseThrow(() -> new RuntimeException("entityNotFound"))).stream()
                .filter(e -> e.getWasteType().equals(wasteType)).findFirst().get();
        boolean result = detectedWasteRepository.findFirstByDetectorAndTimestampIsBetween(detector, LocalDateTime.now().minusSeconds(30), LocalDateTime.now()) != null;
        if(result){
            String email = authenticationService.getUserDetails();
            System.out.println(email);
            User user = userRepository.findByEmail(email).orElse(null);
            if(nonNull(user)) {
                pointService.addPoint(5, user);
            }
        }
        return result;
    }
}
