package mx.com.ml.rebell.alliance.security.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import mx.com.ml.rebell.alliance.security.dto.AccessTokenDto;
import mx.com.ml.rebell.alliance.security.dto.SignInDto;
import mx.com.ml.rebell.alliance.security.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/auth",
    consumes = {MediaType.APPLICATION_JSON_VALUE},
    produces = {MediaType.APPLICATION_JSON_VALUE})
@Api(tags = {"Authentication"})
public class AuthController {

  private final AuthService service;

  @Autowired
  public AuthController(final AuthService service) {
    this.service = service;
  }

  @ApiOperation(
      value = "signIn",
      notes = "Validates user credentials to obtain the access token."
  )
  @PostMapping(path = {"/sign-in"})
  public ResponseEntity<AccessTokenDto> signIn(@RequestBody @Valid final SignInDto signIn) {
    return service.signIn(signIn)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.badRequest().build());
  }
}
