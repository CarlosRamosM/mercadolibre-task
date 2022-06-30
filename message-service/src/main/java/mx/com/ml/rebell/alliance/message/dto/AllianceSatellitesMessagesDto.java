package mx.com.ml.rebell.alliance.message.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "AllianceSatellitesMessages", description = "Información de los satélites aliados.")
public class AllianceSatellitesMessagesDto {

  @ApiModelProperty(name = "satellites", notes = "Satélites aliados")
  private List<AllianceSatellitesDto> satellites;
}
