package com.capgemini.hackyeah.ai.model;

import lombok.Data;

import java.util.List;

@Data
public class ImageResponse {
    private List<Item> items;
    private String status;
    private Double cost;

    @Data
    public static class Item {
        private String label;
        private Double confidence;
        private Double x_min;
        private Double x_max;
        private Double y_min;
        private Double y_max;
    }
}
