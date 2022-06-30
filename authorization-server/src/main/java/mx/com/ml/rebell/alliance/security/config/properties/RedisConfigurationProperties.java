package mx.com.ml.rebell.alliance.security.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
public class RedisConfigurationProperties {

  private String host;

  private int port;
}
