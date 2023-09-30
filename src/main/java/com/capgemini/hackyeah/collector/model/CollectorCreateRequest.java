package com.capgemini.hackyeah.collector.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CollectorCreateRequest {
    private String address;
    private String label;
    private String owner;
}
