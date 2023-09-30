package com.capgemini.hackyeah.ai.service.openapi;

import com.capgemini.hackyeah.ai.configuration.EdenConfig;
import com.capgemini.hackyeah.ai.model.ImageResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageRecognitionAi {
    private final RestTemplate restTemplate = new RestTemplate();
    private final EdenConfig edenConfig;

    public ImageResponse getWastesFromImage(MultipartFile image) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set(HttpHeaders.AUTHORIZATION, edenConfig.getSecret());

        MultiValueMap<String, Object> formdata = new LinkedMultiValueMap<>();
        formdata.put("providers", Collections.singletonList(edenConfig.getProvider()));
        File tempImageFile = null;
        try {
            tempImageFile = Files.write(Files.createTempFile("temp", ".jpg"), image.getBytes()).toFile();
            formdata.add("file", new FileSystemResource(tempImageFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        HttpEntity<MultiValueMap<String, Object>> chat = new HttpEntity<>(formdata, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(edenConfig.getUrl(), chat, String.class);
        return bodyToImageResponse(response.getBody());
    }

    private ImageResponse bodyToImageResponse(String body) {
        body = body.substring(body.indexOf(edenConfig.getProvider()) + 8, body.indexOf("}]}}") + 3);
        log.info(body);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(body, new TypeReference<>() {
            });
        } catch (Exception e) {
            log.error("Error occurred during object mapping", e);
            return new ImageResponse();
        }
    }
}
