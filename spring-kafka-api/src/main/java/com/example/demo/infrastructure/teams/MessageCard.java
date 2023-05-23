package com.example.demo.infrastructure.teams;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageCard {

    @JsonProperty("title")
    private String title;

    @JsonProperty("text")
    private String text;

}
