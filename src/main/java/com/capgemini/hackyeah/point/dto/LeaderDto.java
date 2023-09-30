package com.capgemini.hackyeah.point.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LeaderDto {

    private String firstname;
    private String lastname;
    private Long points;
}
