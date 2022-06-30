package mx.com.ml.rebell.alliance.message.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import mx.com.ml.rebell.alliance.message.dto.base.BaseDto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ApiModel(
    value = "SignIn",
    description = "Credentials to gain access to the secure areas of the platform."
)
public final class SignInDto extends BaseDto {

  @Email(
      message = "error.email.not.valid"
  )
  @NotNull(
      message = "error.auth.username.empty"
  )
  @JsonProperty("username")
  @ApiModelProperty(
      name = "username",
      value = "Username",
      required = true
  )
  private final String username;

  @NotNull(
      message = "error.auth.password.empty"
  )
  @JsonProperty("password")
  @ApiModelProperty(
      name = "password",
      value = "User's password.",
      required = true
  )
  private final String password;
}
