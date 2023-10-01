package com.capgemini.hackyeah.ai.controller;

import com.capgemini.hackyeah.ai.model.AnalyzeRequest;
import com.capgemini.hackyeah.ai.model.AnalyzeResponse;
import com.capgemini.hackyeah.ai.model.DoneRequest;
import com.capgemini.hackyeah.ai.service.OpenAIService;
import com.capgemini.hackyeah.detector.service.DetectorService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
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

    @PostMapping("/done")
    public ResponseEntity<String> generateImages(@RequestBody DoneRequest doneRequest) {

        return detectorService.getDetectorStatus(doneRequest.getWasteType(), doneRequest.getCollectorId()) ?
                new ResponseEntity<>(HttpStatusCode.valueOf(200)) : new ResponseEntity<>(HttpStatusCode.valueOf(201))
                ;
    }

}
