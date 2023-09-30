package com.capgemini.hackyeah.ai.model;

import lombok.Data;

@Data
public class GPTResponse {
    private int paper;
    private int glass;
    private int plasticMetal;
    private int bio;
    private int mixed;
}
