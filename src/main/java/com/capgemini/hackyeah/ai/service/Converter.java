package com.capgemini.hackyeah.ai.service;

import com.capgemini.hackyeah.ai.model.AnalyzeRequest;
import com.capgemini.hackyeah.ai.model.ByteArrayMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

public class Converter {

    public static MultipartFile base64ToMultipartFile(AnalyzeRequest request) {
        MultipartFile file;
        try {
            //split base64 string based on ","
            String[] baseStrs = request.getImage().split(",");

            //decoding the base64 image
            byte[] fileBytes = Base64.getDecoder().decode(request.getImage().trim());

            //creating the MultipartFile using ByteArrayMultipartFile
            file = new ByteArrayMultipartFile(fileBytes, baseStrs[0]);

        } catch (Exception e) {
            throw new IllegalArgumentException("Error occurred while converting base64 to MultipartFile", e);
        }
        return file;
    }
}
