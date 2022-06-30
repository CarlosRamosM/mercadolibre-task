package mx.com.ml.rebell.alliance.security.config.filter;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import mx.com.ml.rebell.alliance.security.config.JwtToken;
import mx.com.ml.rebell.alliance.security.dto.UserSecurityDto;
import mx.com.ml.rebell.alliance.security.service.AuthService;
import mx.com.ml.rebell.alliance.security.util.UserContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

  private final JwtToken jwtToken;

  private final AuthService authService;

  public JwtRequestFilter(final JwtToken jwtToken, final AuthService authService) {
    this.jwtToken = jwtToken;
    this.authService = authService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
    final var requestTokenHeader = request.getHeader("Auth-Token");
    String username = null;
    String token = null;
    if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
      token = requestTokenHeader.replace("Bearer ", "");
      try {
        username = jwtToken.getUsernameFromToken(token);
      } catch (IllegalArgumentException e) {
        log.error("Unable to get JWT Token", e);
      } catch (ExpiredJwtException e) {
        log.error("JWT Token has expired", e);
      }
    } else {
      log.warn("JWT Token does not begin with Bearer String");
    }
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      var userDetails = this.authService.loadUserByUsername(username);
      if (Boolean.TRUE.equals(jwtToken.validateToken(token, userDetails))) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken
            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        request.setAttribute(token, createUserVee(userDetails));
        UserContextHolder.setUser(createUserVee(userDetails));
      }
    }
    chain.doFilter(request, response);
  }

  private UserSecurityDto createUserVee(final UserDetails userDetails) {
    return UserSecurityDto.builder()
        .username(userDetails.getUsername())
        .build();
  }
}
