package com.capgemini.hackyeah.ai.service.openapi;

import com.capgemini.hackyeah.ai.configuration.GPTConfig;
import com.capgemini.hackyeah.ai.model.AnalyzeResponse;
import com.capgemini.hackyeah.ai.model.GPTResponse;
import com.capgemini.hackyeah.ai.model.ImageResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResultInterpreterAi {

    private final RestTemplate restTemplate = new RestTemplate();
    private final GPTConfig gptConfig;

    public AnalyzeResponse fillWasteTypePercentage(ImageResponse imageResponse) {

        ImageResponse.Item item = imageResponse.getItems().stream().max(Comparator.comparing(ImageResponse.Item::getConfidence)).orElseThrow(() -> new EntityNotFoundException("ChatGPT is too lazy"));
        imageResponse.getItems().sort(Comparator.comparing(ImageResponse.Item::getConfidence));
        ResponseEntity<String> chatResponse = objectRecognitionAICall(item);

        String chatBody = chatResponse.getBody();
        System.out.println(chatBody);
        chatBody = chatBody.substring(chatBody.indexOf("paper") - 3, chatBody.indexOf("finish_reason") - 4);
        chatBody = chatBody.replace("\\", "");
        System.out.println(chatBody);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            GPTResponse gptResponse = objectMapper.readValue(chatBody, new TypeReference<>() {
            });

            TreeMap<String, Integer> wastes = createWastes(gptResponse);

            return AnalyzeResponse.builder()
                    .bio(gptResponse.getBio())
                    .paper(gptResponse.getPaper())
                    .mixed(gptResponse.getMixed())
                    .plasticMetal(gptResponse.getPlasticMetal())
                    .glass(gptResponse.getGlass())
                    .wasteItem(item.getLabel())
                    .wasteType(figureWasteType(wastes).toUpperCase())
                    .build();

        } catch (Exception e) {
            log.error("Error occurred during object mapping", e);
        }
        return AnalyzeResponse.builder().build();
    }

    public ResponseEntity<String> objectRecognitionAICall(ImageResponse.Item item) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, new String(Base64.getDecoder().decode(gptConfig.getSecret()), StandardCharsets.UTF_8));


        String label = item.getLabel();
        Double confidence = item.getConfidence();

        String requestJson =
                "{\n" +
                        "  \"model\": \"gpt-3.5-turbo-0613\",\n" +
                        "  \"temperature\": 0.1,\n" +
                        "  \"messages\": [\n" +
                        "    {\n" +
                        "      \"role\": \"system\",\n" +
                        "      \"content\": \"Given the following objects and their confidence levels:\"\n" +
                        "    }" +
                        ",\n" +
                        "    {\n" +
                        "      \"role\": \"user\",\n" +
                        "      \"content\": \"Object: " + label + ", Confidence: " + confidence + "\"\n" +
                        "    }" +
                        ",{\n" +
                        "      \"role\": \"system\",\n" +
                        "      \"content\": \"You need to categorize type of waste for Object. There are 5 types that needs to be considered:Paper, Glass, Plastic&Metal, Bio, Mixed. Any living objects that are living entities should not be interpreted in this manner and be populated with only 00 percentage. Any type of food should be classified as a BIO The format of response should be: {\\\\\\\"paper\\\\\\\":\\\\\\\"XX\\\\\\\",\\\\\\\"glass\\\\\\\":\\\\\\\"XX\\\\\\\",\\\\\\\"plasticMetal\\\\\\\":\\\\\\\"XX\\\\\\\",\\\\\\\"bio\\\\\\\":\\\\\\\"XX\\\\\\\",\\\\\\\"mixed\\\\\\\":\\\\\\\"XX\\\\\\\"}\\\"\\n where each XX contains 2 digits is a percentage in range 00%-99%  of waste type.\"\n" +
                        "}]" +
                        "}";

        HttpEntity<String> chat = new HttpEntity<>(requestJson, headers);
        System.out.println(chat.getBody());
        return restTemplate.postForEntity(gptConfig.getUrl(), chat, String.class);
    }

    private static String figureWasteType(TreeMap<String, Integer> wastes) {

        boolean isHuman = wastes.values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum() == 0;

        return isHuman ? "HUMAN" : wastes.keySet()
                .stream()
                .filter(e -> wastes.get(e) == Collections.max(wastes.values()))
                .findFirst()
                .orElse(null);

    }

    private static TreeMap<String, Integer> createWastes(GPTResponse gptResponse) {
        TreeMap<String, Integer> wastes = new TreeMap<>();
        wastes.put("PAPER", gptResponse.getPaper());
        wastes.put("Bio", gptResponse.getBio());
        wastes.put("Glass", gptResponse.getGlass());
        wastes.put("Plastic_Metal", gptResponse.getPlasticMetal());
        wastes.put("Mixed", gptResponse.getMixed());
        return wastes;
    }
}
