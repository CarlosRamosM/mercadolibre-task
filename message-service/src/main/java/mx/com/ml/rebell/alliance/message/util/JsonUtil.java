package mx.com.ml.rebell.alliance.message.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public abstract class JsonUtil {

  private static final ObjectMapper mapper = new ObjectMapper();

  private JsonUtil() {}

  static {
    mapper
        .findAndRegisterModules()
        .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
        .setSerializationInclusion(JsonInclude.Include.NON_NULL);
  }

  public static Optional<String> asJsonString(final Object object) {
    try {
      return Optional.of(mapper.writeValueAsString(object));
    } catch (final JsonProcessingException e) {
      log.error("Dramatic... {}", e.getMessage(), e);
    }
    return Optional.empty();
  }

  public static <T> Optional<T> fromJson(final String json, final Class<T> clazz) {
    try {
      return Optional.of(mapper.readValue(json, clazz));
    } catch (final IOException e) {
      log.error("Atomic Mushroom for '{}'... {}", json, e.getMessage(), e);
    }
    return Optional.empty();
  }

  public static <T> Optional<T> fromJson(final String json, final com.fasterxml.jackson.core.type.TypeReference<T> typeReference) {
    try {
      return Optional.of(mapper.readValue(json, typeReference));
    } catch (final IOException e) {
      log.error("Atomic Mushroom for '{}'... {}", json, e.getMessage(), e);
    }
    return Optional.empty();
  }

  public static ObjectMapper mapper() {
    return mapper;
  }
}
