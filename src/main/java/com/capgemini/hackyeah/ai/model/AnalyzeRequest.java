package com.capgemini.hackyeah.ai.model;

import lombok.Data;

@Data
public class AnalyzeRequest {
    private String image;
    private Integer collectorId;
}
