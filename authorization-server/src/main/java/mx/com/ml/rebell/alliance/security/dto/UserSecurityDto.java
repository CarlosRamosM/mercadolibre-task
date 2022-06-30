package mx.com.ml.rebell.alliance.security.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import mx.com.ml.rebell.alliance.security.dto.base.BaseDto;

@Builder
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public final class UserSecurityDto extends BaseDto {

  @JsonProperty("username")
  private final String username;
}
