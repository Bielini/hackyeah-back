package com.capgemini.hackyeah.collector.controller;

import com.capgemini.hackyeah.collector.model.CollectorCreateRequest;
import com.capgemini.hackyeah.collector.model.CollectorDto;
import com.capgemini.hackyeah.collector.service.CollectorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/users/{userId}/collectors")
@RequiredArgsConstructor
public class CollectorController {

    private final CollectorService collectorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registerCollector(@RequestBody CollectorCreateRequest request, @PathVariable Integer userId) {
        collectorService.registerCollector(request, userId);
    }

    @GetMapping
    public List<CollectorDto> getUserCollectors(@PathVariable Integer userId) {
        return collectorService.getUserCollectors(userId);
    }

    @GetMapping("{collectorId}/code")
    public ResponseEntity<byte[]> generateQRCode(@PathVariable Integer collectorId) {
        return collectorService.generateCode(collectorId);
    }

}
