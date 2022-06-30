package mx.com.ml.rebell.alliance.message.serive;

import lombok.extern.slf4j.Slf4j;
import mx.com.ml.rebell.alliance.message.dto.UserSecurityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

@Slf4j
@Service
public class AuthService implements UserDetailsService {

  private final RedisService redisService;

  @Autowired
  public AuthService(final RedisService redisService) {
    this.redisService = redisService;
  }

  @Override
  public UserDetails loadUserByUsername(final String token) throws UsernameNotFoundException {
    final var user = (UserSecurityDto) redisService.getValue(token, UserSecurityDto.class)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "error.session.user.not.found"));
    return createUserDetails(user);
  }

  public UserDetails createUserDetails(final UserSecurityDto supplierDetail) {
    final GrantedAuthority authority = new SimpleGrantedAuthority("USER");
    return new User(supplierDetail.getUsername(), supplierDetail.getUsername(), Collections.singletonList(authority));
  }
}
