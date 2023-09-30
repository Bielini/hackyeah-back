package com.capgemini.hackyeah.point.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class LeaderDto {

    private String firstname;
    private String lastname;
    private Long points;
}
