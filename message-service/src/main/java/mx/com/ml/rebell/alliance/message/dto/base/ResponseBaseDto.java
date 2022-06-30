package mx.com.ml.rebell.alliance.message.dto.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponseBaseDto extends BaseDto implements Serializable {

  @JsonProperty(
      value = "status",
      required = true,
      index = 0
  )
  @ApiModelProperty(
      name = "status",
      notes = "Transfer status.",
      required = true
  )
  private String status;

  @JsonProperty(
      value = "errors",
      index = 1
  )
  @ApiModelProperty(
      name = "error",
      notes = "List of transfer errors.",
      position = 1
  )
  private List<String> errors;
}
