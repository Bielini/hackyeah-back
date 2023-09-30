package com.capgemini.hackyeah.detector.model;

import com.capgemini.hackyeah.domain.model.WasteType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DetectorDto {
    private WasteType wasteType;
    private Integer id;
    private boolean active;
}
