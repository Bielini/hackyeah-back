package com.capgemini.hackyeah.ai.model;

import com.capgemini.hackyeah.domain.model.WasteType;
import lombok.Data;

@Data
public class DoneRequest {

    WasteType wasteType;
    Integer collectorId;
}
