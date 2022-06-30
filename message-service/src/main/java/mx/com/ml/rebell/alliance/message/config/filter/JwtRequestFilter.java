package mx.com.ml.rebell.alliance.message.config.filter;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import mx.com.ml.rebell.alliance.message.config.JwtToken;
import mx.com.ml.rebell.alliance.message.dto.UserSecurityDto;
import mx.com.ml.rebell.alliance.message.serive.AuthService;
import mx.com.ml.rebell.alliance.message.util.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

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

  private final HandlerExceptionResolver resolver;

  @Autowired
  public JwtRequestFilter(final JwtToken jwtToken,
                          final AuthService authService,
                          @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
    this.jwtToken = jwtToken;
    this.authService = authService;
    this.resolver = resolver;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
    final String requestTokenHeader = request.getHeader("Auth-Token");
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
        resolver.resolveException(request, response, null, e);
      }
    } else {
      log.warn("JWT Token does not begin with Bearer String");
    }
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      log.info("JwtRequestFilter username: {}", username);
      UserDetails userDetails = this.authService.loadUserByUsername(requestTokenHeader);
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
    var user = new UserSecurityDto();
    user.setUsername(userDetails.getUsername());
    return user;
  }
}
