package com.example.demo.common.utils;

import static org.assertj.core.api.Assertions.*;

import com.example.demo.presentation.TopicBody;
import com.example.demo.presentation.TopicEnvelope;
import com.example.demo.presentation.TopicHeader;
import org.junit.jupiter.api.Test;

class JsonUtilsTest {

  @Test
  void 適切にオブジェクトをJSONにマーシャルできる場合() {
    // setup
    TopicEnvelope envelope = new TopicEnvelope(new TopicHeader("0001", "2023/05/20", true),
                                               new TopicBody("Hello World."));

    // execute
    String actual = JsonUtils.marshalToJson(envelope);

    // assert
    String expected = """
            {
              "header" : {
                "id" : "0001",
                "timestamp" : "2023/05/20",
                "importance" : true
              },
              "body" : {
                "text" : "Hello World."
              }
            }""";
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void 適切にJSONからオブジェクトにアンマーシャルできる場合() {
    // setup
    String json = """
            {
              "header" : {
                "id" : "0001",
                "timestamp" : "2023/05/20",
                "importance" : true
              },
              "body" : {
                "text" : "Hello World."
              }
            }""";

    // execute
    TopicEnvelope actual = JsonUtils.unmarshalFromJson(json, TopicEnvelope.class);

    // assert
    TopicEnvelope expected = new TopicEnvelope(new TopicHeader("0001", "2023/05/20", true),
                                               new TopicBody("Hello World."));
    assertThat(actual).isEqualTo(expected);
  }

}