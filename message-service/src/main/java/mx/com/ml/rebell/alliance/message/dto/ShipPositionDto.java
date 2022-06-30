package mx.com.ml.rebell.alliance.message.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import mx.com.ml.rebell.alliance.message.dto.base.BaseDto;

@Data
@NoArgsConstructor
public class ShipPositionDto extends BaseDto {

  private PositionDto position;

  private String message;
}
