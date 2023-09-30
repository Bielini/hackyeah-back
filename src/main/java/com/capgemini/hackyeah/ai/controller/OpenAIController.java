package com.capgemini.hackyeah.ai.controller;

import com.capgemini.hackyeah.ai.model.AnalyzeRequest;
import com.capgemini.hackyeah.ai.model.AnalyzeResponse;
import com.capgemini.hackyeah.ai.service.OpenAIService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@FieldDefaults(makeFinal = true, level = AccessLevel.PUBLIC)
public class OpenAIController {

    OpenAIService openAIService;

    @PostMapping("/analyze")
    public AnalyzeResponse generateImages(@RequestBody String image) {
        AnalyzeRequest request = new AnalyzeRequest();
        request.setCollectorId(1);
        request.setImage(image);
        return openAIService.getWasteSuggestion(request);
    }

}
