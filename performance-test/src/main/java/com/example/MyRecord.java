package com.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MyRecord {

  @JsonProperty("messageId")
  private long messageId;

  @JsonProperty("messageBody")
  private String messageBody;

  @JsonProperty("messageStatus")
  private String messageStatus;

  @JsonProperty("messageCategory")
  private String messageCategory;

  @JsonProperty("messageTime")
  private long messageTime;

}
