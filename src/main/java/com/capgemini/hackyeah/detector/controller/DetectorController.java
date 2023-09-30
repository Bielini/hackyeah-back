package com.capgemini.hackyeah.detector.controller;


import com.capgemini.hackyeah.detector.model.DetectorCreateRequest;
import com.capgemini.hackyeah.detector.service.DetectorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users/{userId}/collectors/{collectorId}/detectors")
@RequiredArgsConstructor
public class DetectorController {

    private final DetectorService detectorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registerDetector(@RequestBody DetectorCreateRequest request, @PathVariable Integer collectorId) {
        detectorService.registerDetector(request, collectorId);
    }

    @PatchMapping("/{detectorId}")
    public void activateDetector(@PathVariable Integer detectorId){
        detectorService.activateDetector(detectorId);
    }
}
