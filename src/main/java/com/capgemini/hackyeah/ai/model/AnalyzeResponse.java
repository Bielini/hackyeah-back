package com.capgemini.hackyeah.ai.model;

import lombok.Builder;
import lombok.Data;

import java.io.File;

@Data
@Builder
public class AnalyzeResponse {
    private int bio;
    private int glass;
    private int mixed;
    private int plasticMetal;
    private int paper;
    private File image;
    private String wasteItem;
    private String wasteType;
}
