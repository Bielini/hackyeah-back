package com.capgemini.hackyeah.detectedwaste.controller;


import com.capgemini.hackyeah.detectedwaste.service.DetectedWasteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/detected-waste/{detectorId}")
@RequiredArgsConstructor
public class DetectedWasteController {

    private final DetectedWasteService detectedWasteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void wasteDetection(@PathVariable Integer detectorId) {
        detectedWasteService.registerDetector(detectorId);
    }

}
