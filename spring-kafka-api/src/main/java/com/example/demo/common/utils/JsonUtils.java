package com.example.demo.common.utils;

import static java.util.Objects.nonNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class JsonUtils {

  public static <T> T unmarshalFromJson(String json, Class<T> clazz) {
    T result = null;

    if (nonNull(json)) {
      try {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        result = mapper.readValue(json, clazz);
      } catch (IOException e) {
        log.warn("Unmarshaling from json is failed.", e);
      }
    }

    return result;
  }

  public static String marshalToJson(Object obj) {
    String result = null;

    try {
      if (nonNull(obj)) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.registerModule(new JavaTimeModule());
        result = mapper.writeValueAsString(obj);
      } else {
        result = "{}";
      }
    } catch (JsonProcessingException e) {
      log.warn("Marshaling to json is failed.", e);
    }

    return result;
  }

}
