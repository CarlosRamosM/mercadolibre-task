package mx.com.ml.rebell.alliance.message.dto;

import lombok.Data;

import java.util.List;

@Data
public class AllianceSatellitesDto extends PositionDto {

  private String name;

  private double distance;

  private List<String> message;
}
