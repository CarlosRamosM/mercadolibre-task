package mx.com.ml.rebell.alliance.message.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "alliance")
public class AllianceSatellitePositionProperties {

  private Map<String, SatelliteCoordinates> satellites;
}
