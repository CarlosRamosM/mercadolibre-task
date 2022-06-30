package mx.com.ml.rebell.alliance.message.serive;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RedisService {

  private final ObjectMapper mapper;

  private final RedisTemplate< String, Object> template;

  @Autowired
  public RedisService(final ObjectMapper mapper, final RedisTemplate<String, Object> redisTemplate) {
    this.mapper = mapper;
    this.template = redisTemplate;
  }

  public synchronized List<String> getKeys(final String pattern) {
    template.setHashValueSerializer(new StringRedisSerializer());
    template.setValueSerializer(new StringRedisSerializer());
    final Set<String> redisKeys = template.keys(pattern);
    assert redisKeys != null;
    return new ArrayList<>(redisKeys);
  }

  public synchronized Object getValue(final String key) {
    template.setHashValueSerializer(new StringRedisSerializer());
    template.setValueSerializer(new StringRedisSerializer());
    return template.opsForValue().get(key);
  }

  public synchronized <T> Optional<Object> getValue(final String key, Class<T> clazz) {
    template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
    template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
    Object obj = template.opsForValue().get(key);
    return obj != null ? Optional.of(mapper.convertValue(obj, clazz)) : Optional.empty();
  }

  public void setValue(final String key, final Object value) {
    setValue(key, value, TimeUnit.MINUTES, 30, false);
  }

  public void setValue(final String key, final Object value, TimeUnit unit, long timeout) {
    setValue(key, value, unit, timeout, false);
  }

  public void setValue(final String key, final Object value, boolean marshal) {
    validateMarshal(key, value, marshal);
    template.expire(key, 30, TimeUnit.MINUTES);
  }

  public void setValue(final String key, final Object value, TimeUnit unit, long timeout, boolean marshal) {
    validateMarshal(key, value, marshal);
    template.expire(key, timeout, unit);
  }

  public void deleteKey(final String key) {
    template.delete(key);
  }

  private void validateMarshal(String key, Object value, boolean marshal) {
    if (marshal) {
      template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
      template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
    } else {
      template.setHashValueSerializer(new StringRedisSerializer());
      template.setValueSerializer(new StringRedisSerializer());
    }
    template.opsForValue().set(key, value);
  }
}
