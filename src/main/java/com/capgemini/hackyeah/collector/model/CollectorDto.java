package com.capgemini.hackyeah.collector.model;

import com.capgemini.hackyeah.detector.model.DetectorDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CollectorDto {
    private Integer id;
    List<DetectorDto> detectors;
    private String label;
    private String address;
    private String owner;
}
