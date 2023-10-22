package com.hapjusil.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PracticeRoomRequestDTO {
    private String name;
    private String thumbnail;
    private String phoneNumber;
    private String website;
    private String location;
    private double rate;
}