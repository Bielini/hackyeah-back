package com.capgemini.hackyeah.ai.controller.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Base64ObjectStorage {

    public String loadTxtFile(String fileName) throws IOException {

        Path path = Paths.get("src/test/resources/" + fileName + ".txt");

        BufferedReader reader = Files.newBufferedReader(path);
        String line = reader.readLine();
        return line;
    }

}
