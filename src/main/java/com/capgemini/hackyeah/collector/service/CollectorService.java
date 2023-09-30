package com.capgemini.hackyeah.collector.service;

import com.capgemini.hackyeah.collector.model.CollectorCreateRequest;
import com.capgemini.hackyeah.collector.model.CollectorDto;
import com.capgemini.hackyeah.detector.service.DetectorService;
import com.capgemini.hackyeah.domain.model.Collector;
import com.capgemini.hackyeah.domain.repository.CollectorRepository;
import com.capgemini.hackyeah.domain.repository.UserRepository;
import com.google.zxing.WriterException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CollectorService {

    private final CollectorRepository repository;
    private final UserRepository userRepository;
    private final DetectorService detectorService;
    private final QRCodeService qrCodeService;

    public void registerCollector(CollectorCreateRequest request, Integer userId) {
        Collector collector = Collector.builder()
                .address(request.getAddress())
                .label(request.getLabel())
                .owner(request.getOwner())
                .user(userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new EntityNotFoundException("User not found")))
                .build();
        log.info(request.toString());
        repository.save(collector);
    }

    public List<CollectorDto> getUserCollectors(Integer userId) {
        return repository.getAllByUser(userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new EntityNotFoundException("User not found  "))).stream()
                .map(this::mapToDto).collect(Collectors.toList());
    }

    private CollectorDto mapToDto(Collector collector) {
        return CollectorDto.builder()
                .address(collector.getAddress())
                .owner(collector.getOwner())
                .label(collector.getLabel())
                .id(collector.getId())
                .detectors(detectorService.getDetectorsForCollector(collector.getId()))
                .build();
    }

    public ResponseEntity<byte[]> generateCode(Integer collectorId) {
        try {
            byte[] qrCode = qrCodeService.generateQRCodeImage(collectorId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);

            return new ResponseEntity<>(qrCode, headers, HttpStatus.OK);
        } catch (WriterException | IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
