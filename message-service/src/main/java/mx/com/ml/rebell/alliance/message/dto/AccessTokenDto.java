package mx.com.ml.rebell.alliance.message.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import mx.com.ml.rebell.alliance.message.dto.base.BaseDto;

@Builder
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ApiModel(
    value = "AccessToken",
    description = "Access information to the secure area of the platform."
)
public final class AccessTokenDto extends BaseDto {

  @JsonProperty("token")
  @ApiModelProperty(
      name = "token",
      notes = "Access token to the secure section of the platform."
  )
  private final String token;

  @JsonProperty("refreshToken")
  @ApiModelProperty(
      name = "refreshToken",
      notes = "Access token to extend the session after expiration of the main token."
  )
  private final String refreshToken;
}
