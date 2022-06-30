package mx.com.ml.rebell.alliance.security.service;

import lombok.extern.slf4j.Slf4j;
import mx.com.ml.rebell.alliance.security.config.JwtToken;
import mx.com.ml.rebell.alliance.security.dm.model.User;
import mx.com.ml.rebell.alliance.security.dm.repository.UserRepository;
import mx.com.ml.rebell.alliance.security.dto.AccessTokenDto;
import mx.com.ml.rebell.alliance.security.dto.SignInDto;
import mx.com.ml.rebell.alliance.security.dto.UserSecurityDto;
import mx.com.ml.rebell.alliance.security.util.EncryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AuthService implements UserDetailsService {

  private final UserRepository repository;

  private final JwtToken jwtToken;

  private final RedisService redisService;

  @Autowired
  public AuthService(final UserRepository repository, final JwtToken jwtToken, final RedisService redisService) {
    this.repository = repository;
    this.jwtToken = jwtToken;
    this.redisService = redisService;
  }

  /**
   * Validates user credentials to obtain the access token.
   * @param signIn User credentials.
   * @return Access token.
   */
  public Optional<AccessTokenDto> signIn(final SignInDto signIn) {
    log.info("Sign In user: {}", signIn.getUsername());
    var user = repository.findByUsernameIgnoreCase(signIn.getUsername())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "error.user.not.found"));
    if (!EncryptUtil.validateKey(signIn.getPassword(), user.getPassword())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "error.auth.user.password.wrong");
    }
    return getAccessTokenDto(user);
  }

  /**
   * Searches for user information by username.
   * @param username User.
   * @return User information.
   * @throws UsernameNotFoundException User not found.
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var user = repository.findByUsernameIgnoreCase(username)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "error.user.not.found"));
    return createUserDetails(user);
  }

  private Optional<AccessTokenDto> getAccessTokenDto(final User user) {
    final UserDetails userDetails = createUserDetails(user);
    final String authToken = jwtToken.generateToken(userDetails);
    final String refreshToken = jwtToken.generateRefreshToken(userDetails);
    redisService.setValue(authToken, createUser(user.getUsername()), TimeUnit.SECONDS, 3600L, true);
    redisService.setValue(refreshToken, authToken, TimeUnit.SECONDS, 3600L, true);
    return Optional.of(AccessTokenDto
        .builder()
        .token(authToken)
        .refreshToken(refreshToken)
        .build()
    );
  }

  private UserDetails createUserDetails(User user) {
    final GrantedAuthority authority = new SimpleGrantedAuthority("USER");
    return new org.springframework.security.core.userdetails.User(
        user.getUsername(),
        user.getPassword(),
        Collections.singletonList(authority)
    );
  }

  private UserSecurityDto createUser(final String username) {
    return UserSecurityDto
        .builder()
        .username(username)
        .build();
  }
}
