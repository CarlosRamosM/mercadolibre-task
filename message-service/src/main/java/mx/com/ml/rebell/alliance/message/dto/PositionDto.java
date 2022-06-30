package mx.com.ml.rebell.alliance.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.com.ml.rebell.alliance.message.dto.base.BaseDto;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PositionDto extends BaseDto {

  private double x;

  private double y;
}
