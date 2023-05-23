package com.example.demo.infrastructure.kafka;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicEnvelope {

  @JsonProperty("header")
  TopicHeader header;

  @JsonProperty("body")
  TopicBody body;

}
