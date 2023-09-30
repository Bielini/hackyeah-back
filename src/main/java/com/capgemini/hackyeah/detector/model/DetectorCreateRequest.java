package com.capgemini.hackyeah.detector.model;

import com.capgemini.hackyeah.domain.model.WasteType;
import lombok.Data;

@Data
public class DetectorCreateRequest {
    private WasteType wasteType;
}
