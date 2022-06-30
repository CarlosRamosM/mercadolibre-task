package mx.com.ml.rebell.alliance.message.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mx.com.ml.rebell.alliance.message.dto.base.BaseDto;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserSecurityDto extends BaseDto {

  private String username;
}

