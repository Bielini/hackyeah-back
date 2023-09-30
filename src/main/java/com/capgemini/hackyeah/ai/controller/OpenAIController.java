package com.capgemini.hackyeah.ai.controller;

import com.capgemini.hackyeah.ai.model.AnalyzeRequest;
import com.capgemini.hackyeah.ai.model.AnalyzeResponse;
import com.capgemini.hackyeah.ai.service.OpenAIService;
import com.capgemini.hackyeah.collector.service.CollectorService;
import com.capgemini.hackyeah.detector.controller.DetectorController;
import com.capgemini.hackyeah.detector.service.DetectorService;
import com.capgemini.hackyeah.domain.model.WasteType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@FieldDefaults(makeFinal = true, level = AccessLevel.PUBLIC)
public class OpenAIController {

    OpenAIService openAIService;
    DetectorService detectorService;

    @PostMapping("/analyze")
    public AnalyzeResponse generateImages(@RequestBody AnalyzeRequest request) {
        request.setCollectorId(1);
        return openAIService.getWasteSuggestion(request);
    }

    @GetMapping("/done")
    public boolean generateImages(@RequestParam WasteType wasteType, @RequestParam Integer collectorId) {
        return detectorService.getDetectorStatus(wasteType, collectorId);
    }

}
