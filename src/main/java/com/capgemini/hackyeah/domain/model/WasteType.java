package com.capgemini.hackyeah.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WasteType {
    PAPER, GLASS, PLASTIC_METAL, BIO, MIXED;

    @JsonValue
    public String toValue() {
        return this.name();
    }

    @JsonCreator
    public static WasteType forValue(String value) {
        for (WasteType type : values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        // Set a default value here
        return MIXED;
    }
}
