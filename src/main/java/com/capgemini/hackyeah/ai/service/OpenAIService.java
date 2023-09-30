package com.capgemini.hackyeah.ai.service;

import com.capgemini.hackyeah.ai.model.AnalyzeRequest;
import com.capgemini.hackyeah.ai.model.AnalyzeResponse;
import com.capgemini.hackyeah.ai.model.ImageResponse;
import com.capgemini.hackyeah.ai.service.openapi.ImageRecognitionAi;
import com.capgemini.hackyeah.ai.service.openapi.ResultInterpreterAi;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OpenAIService {

    private final ImageRecognitionAi imageRecognitionAi;
    private final ResultInterpreterAi resultInterpreterAi;

    public AnalyzeResponse getWasteSuggestion(AnalyzeRequest request) {
        MultipartFile file = Converter.base64ToMultipartFile(request);
        ImageResponse imageResponse = imageRecognitionAi.getWastesFromImage(file);
        AnalyzeResponse analyzeResponse = resultInterpreterAi.fillWasteTypePercentage(imageResponse);
        log.info(imageResponse.getStatus());
        return analyzeResponse;

    }

}
