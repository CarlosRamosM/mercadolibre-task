package mx.com.ml.rebell.alliance.security.config;

import lombok.extern.slf4j.Slf4j;
import mx.com.ml.rebell.alliance.security.config.properties.RedisConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

@Slf4j
@Configuration
public class RedisConfig {

  private final RedisConfigurationProperties properties;

  public RedisConfig(final RedisConfigurationProperties properties) {
    this.properties = properties;
  }

  @Bean
  JedisConnectionFactory jedisConnectionFactory() {
    log.info("Redis Config: Host: {}, Port: {}", properties.getHost(), properties.getPort());
    var configuration = new RedisStandaloneConfiguration(properties.getHost(), properties.getPort());
    return new JedisConnectionFactory(configuration);
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate() {
    final RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(jedisConnectionFactory());
    template.setValueSerializer(new GenericToStringSerializer<>(Object.class));
    return template;
  }
}
