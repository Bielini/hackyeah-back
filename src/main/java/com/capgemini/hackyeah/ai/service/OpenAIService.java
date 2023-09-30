package com.capgemini.hackyeah.ai.service;

import com.capgemini.hackyeah.ai.configuration.EdenConfig;
import com.capgemini.hackyeah.ai.configuration.GPTConfig;
import com.capgemini.hackyeah.ai.model.AnalyzeRequest;
import com.capgemini.hackyeah.ai.model.AnalyzeResponse;
import com.capgemini.hackyeah.ai.model.GPTResponse;
import com.capgemini.hackyeah.ai.model.ImageResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OpenAIService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final EdenConfig edenConfig;
    private final GPTConfig gptConfig;

    public AnalyzeResponse getWasteSuggestion(AnalyzeRequest request) {
//        log.trace(request.getImage());
        MultipartFile file = Converter.base64ToMultipartFile(request);

        String body = getWastesFromImage(file);
        ImageResponse imageResponse = bodyToImageResponse(body);
        AnalyzeResponse analyzeResponse = fillWasteTypePercentage(imageResponse);
        log.info(imageResponse.getStatus());

        return analyzeResponse;

    }


    private AnalyzeResponse fillWasteTypePercentage(ImageResponse imageResponse) {
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

    private ResponseEntity<String> objectRecognitionAICall(ImageResponse.Item item) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, gptConfig.getSecret());


        String label = item.getLabel();
        Double confidence = item.getConfidence();

        String requestJson =
                "{\n" +
                        "  \"model\": \"gpt-3.5-turbo\",\n" +
                        "  \"messages\": [\n" +
                        "    {\n" +
                        "      \"role\": \"system\",\n" +
                        "      \"content\": \"Given the following objects and their confidence levels:\"\n" +
                        "    }" +
                        ",\n" +
                        "    {\n" +
                        "      \"role\": \"user\",\n" +
                        "      \"content\": \"- Object: " + label + ", Confidence: " + confidence + "\"\n" +
                        "    }" +
                        ",{\n" +
                        "      \"role\": \"system\",\n" +
                        "      \"content\": \"Provide percentage of how likely this object are included in every waste type - Paper, Glass, Paper&Metal, Bio, Mixed (00-99%)." +
                        "                     Percent - always contains two numbers. Any living entity (e.g. human, animal), should not be interpreted in this manner. - every percentage in this case should be 00 (like lady, human, cat, dog etc.). Every not alive object CAN be interpreted as wastes. The format is next: {\\\"paper\\\":\\\"XX\\\",\\\"glass\\\":\\\"XX\\\",\\\"plasticMetal\\\":\\\"XX\\\",\\\"bio\\\":\\\"XX\\\",\\\"mixed\\\":\\\"XX\\\"}\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"temperature\": 0.2\n" +
                        "}";

        HttpEntity<String> chat = new HttpEntity<>(requestJson, headers);
        System.out.println(chat.getBody());
        return restTemplate.postForEntity(gptConfig.getUrl(), chat, String.class);
    }

    private ImageResponse bodyToImageResponse(String body) {
//        log.info(body);
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

    public String getWastesFromImage(MultipartFile image) {
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
        return response.getBody();
    }

    private void generateImageTemplate(MultipartFile file, AnalyzeResponse response, ImageResponse imageResponse) throws IOException {
        BufferedImage mainImage = ImageIO.read(file.getInputStream());

        int width = mainImage.getWidth();
        int height = mainImage.getHeight();

        // Create a BufferedImage with transparency
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Create a Graphics2D object to draw on the image
        Graphics2D g2d = image.createGraphics();

        // Clear the image with transparency
        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(0, 0, width, height);

        // Set the transparency to 50% (128 out of 255)
        AlphaComposite transparency = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
        g2d.setComposite(transparency);

        imageResponse.getItems().forEach(item -> {
            // Draw a transparent rectangle on the image
            g2d.setColor(Color.ORANGE);
            int x = (int) (width / 100 * (item.getX_min() * 100));
            int y = (int) (height / 100 * (item.getY_min() * 100));
            int xMax = (int) (width / 100 * (item.getX_max() * 100)) - x;
            int yMax = (int) (height / 100 * (item.getY_max() * 100)) - y;
            g2d.fillRect(x, y, xMax, yMax);
        });

        // Dispose the Graphics2D object
        g2d.dispose();

        // Save the image as a PNG file
        try {
            File output = new File("output.jpeg");
            ImageIO.write(image, "jpeg ", output);
            response.setImage(output);
        } catch (IOException e) {
            System.out.println("Error saving image: " + e.getMessage());
        }
    }

}
