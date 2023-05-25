package com.example.demo.presentation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicHeader {

  @JsonProperty("id")
  private String id;

  @JsonProperty("timestamp")
  private String timestamp;

}
