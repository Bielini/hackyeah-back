package com.capgemini.hackyeah.ai.model;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class ByteArrayMultipartFile implements MultipartFile {
    private final byte[] imgContent;
    private final String header;

    public ByteArrayMultipartFile(byte[] imgContent, String header) {
        this.imgContent = imgContent;
        this.header = header.split(";")[0].split("/")[1];
    }

    @Override
    public String getName() {
        return System.currentTimeMillis() + "." + header;
    }

    @Override
    public String getOriginalFilename() {
        return System.currentTimeMillis() + "." + header;
    }

    @Override
    public String getContentType() {
        return "image/" + header;
    }

    @Override
    public boolean isEmpty() {
        return imgContent == null || imgContent.length == 0;
    }

    @Override
    public long getSize() {
        return imgContent.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return imgContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(imgContent);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        new FileOutputStream(dest).write(imgContent);
    }
}