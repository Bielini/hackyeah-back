package com.capgemini.hackyeah.ai.controller;

import com.capgemini.hackyeah.ai.controller.helper.Base64ObjectStorage;
import com.capgemini.hackyeah.ai.model.AnalyzeRequest;
import com.capgemini.hackyeah.ai.model.AnalyzeResponse;
import com.capgemini.hackyeah.ai.service.OpenAIService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.stream.Stream;

@SpringBootTest
class OpenAIControllerTest {
    @Autowired
    OpenAIService openAIService;

    private static Stream<Arguments> provideStringsForIsBlank() {
        return Stream.of(
                Arguments.of("BOTTLE", "PLASTIC_METAL"),
                Arguments.of("CHICKEN", "BIO"),
                Arguments.of("TISSUE", "PAPER"),
                Arguments.of("METAL", "PLASTIC_METAL"),
                Arguments.of("DOG", "HUMAN"),
                Arguments.of("GRANNY", "HUMAN")
        );
    }

    @Disabled
    @ParameterizedTest
    @MethodSource("provideStringsForIsBlank")
    public void openAIControllerTest(String filename, String expected) throws IOException, InterruptedException {
        AnalyzeRequest analyzeRequest = new AnalyzeRequest();
        analyzeRequest.setCollectorId(1);
        analyzeRequest.setImage(new Base64ObjectStorage().loadTxtFile(filename));
        AnalyzeResponse wasteDetectionResult = openAIService.getWasteSuggestion(analyzeRequest);

        Assertions.assertEquals(expected, wasteDetectionResult.getWasteType());
        Thread.sleep(18_000);
    }
}